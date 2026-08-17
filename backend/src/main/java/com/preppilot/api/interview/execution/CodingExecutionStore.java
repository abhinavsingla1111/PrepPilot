package com.preppilot.api.interview.execution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.interview.CodingLanguage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class CodingExecutionStore {

    private final CodingExecutionSubmissionRepository repository;
    private final CodingTestCaseParser testCaseParser;
    private final ObjectMapper objectMapper;
    private final RunnerProperties properties;
    private final SecureRandom secureRandom;
    private final Clock clock;

    public CodingExecutionStore(
            CodingExecutionSubmissionRepository repository,
            CodingTestCaseParser testCaseParser,
            ObjectMapper objectMapper,
            RunnerProperties properties,
            SecureRandom secureRandom,
            Clock clock
    ) {
        this.repository = repository;
        this.testCaseParser = testCaseParser;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.secureRandom = secureRandom;
        this.clock = clock;
    }

    @Transactional
    public Work claim() {
        Instant now = clock.instant();
        repository.failExhaustedLeases(
                now,
                properties.maxAttempts(),
                "The local runner could not finish this job after several attempts."
        );
        CodingExecutionSubmission submission = repository
                .claimCandidate(now, properties.maxAttempts())
                .orElse(null);
        if (submission == null) return null;

        String leaseToken = randomToken();
        submission.claim(hash(leaseToken), now, now.plus(properties.leaseDuration()));
        repository.save(submission);
        List<CodeExecutionGateway.ExecutionCase> cases = testCaseParser.parse(submission.getSession().getPrompt());
        return new Work(
                submission.getId(), leaseToken, submission.getLanguage(), submission.getSourceCode(),
                IntStream.range(0, cases.size())
                        .mapToObj(index -> new WorkerCase(index + 1, cases.get(index).input()))
                        .toList()
        );
    }

    @Transactional
    public Completion complete(UUID submissionId, String leaseToken, List<WorkerCaseResult> workerResults) {
        CodingExecutionSubmission submission = requireActiveLease(submissionId, leaseToken);
        List<CodeExecutionGateway.ExecutionCase> cases = testCaseParser.parse(submission.getSession().getPrompt());
        List<CodeExecutionGateway.CaseOutcome> outcomes = validateAndMapResults(cases, workerResults);
        int passed = (int) outcomes.stream().filter(outcome -> outcome.status() == CodeExecutionCaseStatus.PASSED).count();
        submission.complete(passed, write(outcomes), clock.instant());
        repository.save(submission);
        return new Completion(
                submission.getId(), submission.getUser().getEmail(), submission.getSession().getPrompt().getTitle(),
                submission.getStatus(), passed, submission.getTotalTests()
        );
    }

    @Transactional
    public void fail(UUID submissionId, String leaseToken, String message) {
        CodingExecutionSubmission submission = requireActiveLease(submissionId, leaseToken);
        submission.fail(safeFailure(message), clock.instant());
        repository.save(submission);
    }

    public List<CodeExecutionGateway.CaseOutcome> readOutcomes(CodingExecutionSubmission submission) {
        try {
            return objectMapper.readValue(submission.getResultsJson(), new TypeReference<>() {});
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored execution results are invalid.", exception);
        }
    }

    private String write(List<CodeExecutionGateway.CaseOutcome> outcomes) {
        try {
            return objectMapper.writeValueAsString(outcomes);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not store execution results.", exception);
        }
    }

    private static String safeFailure(String message) {
        if (message == null || message.isBlank()) return "The local runner could not complete this run.";
        String sanitized = message.replace('\r', ' ').replace('\n', ' ').trim();
        return sanitized.length() <= 500 ? sanitized : sanitized.substring(0, 500);
    }

    private CodingExecutionSubmission requireActiveLease(UUID submissionId, String leaseToken) {
        CodingExecutionSubmission submission = repository.findById(submissionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Code run not found."));
        Instant now = clock.instant();
        boolean active = submission.getStatus() == CodingExecutionStatus.RUNNING
                && submission.getLeaseExpiresAt() != null
                && !submission.getLeaseExpiresAt().isBefore(now)
                && secureEquals(submission.getLeaseTokenHash(), hash(leaseToken));
        if (!active) {
            throw new ApiException(HttpStatus.CONFLICT, "This code-run lease is no longer active.");
        }
        return submission;
    }

    private List<CodeExecutionGateway.CaseOutcome> validateAndMapResults(
            List<CodeExecutionGateway.ExecutionCase> cases,
            List<WorkerCaseResult> workerResults
    ) {
        if (workerResults == null || workerResults.size() != cases.size()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "The runner returned an incomplete result set.");
        }
        List<WorkerCaseResult> ordered = workerResults.stream()
                .sorted(Comparator.comparingInt(WorkerCaseResult::position))
                .toList();
        List<CodeExecutionGateway.CaseOutcome> outcomes = new ArrayList<>(cases.size());
        for (int index = 0; index < cases.size(); index++) {
            WorkerCaseResult result = ordered.get(index);
            if (result.position() != index + 1) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "The runner returned invalid test positions.");
            }
            CodeExecutionGateway.ExecutionCase test = cases.get(index);
            String actual = truncate(result.actualOutput(), properties.maximumOutputCharacters());
            CodeExecutionCaseStatus status = switch (result.status()) {
                case EXECUTED -> normalized(actual).equals(normalized(test.expectedOutput()))
                        ? CodeExecutionCaseStatus.PASSED : CodeExecutionCaseStatus.WRONG_ANSWER;
                case COMPILE_ERROR -> CodeExecutionCaseStatus.COMPILE_ERROR;
                case RUNTIME_ERROR -> CodeExecutionCaseStatus.RUNTIME_ERROR;
                case TIME_LIMIT -> CodeExecutionCaseStatus.TIME_LIMIT;
                case INTERNAL_ERROR -> CodeExecutionCaseStatus.INTERNAL_ERROR;
            };
            outcomes.add(new CodeExecutionGateway.CaseOutcome(
                    index + 1,
                    test.visible(),
                    status,
                    test.visible() ? test.input() : null,
                    test.visible() ? test.expectedOutput() : null,
                    test.visible() ? actual : null,
                    truncate(result.diagnostic(), properties.maximumDiagnosticCharacters()),
                    truncate(result.timeSeconds(), 20),
                    result.memoryKilobytes()
            ));
        }
        return List.copyOf(outcomes);
    }

    private String randomToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hash(String value) {
        try {
            byte[] hashed = MessageDigest.getInstance("SHA-256")
                    .digest((value == null ? "" : value).getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    private static boolean secureEquals(String expected, String actual) {
        if (expected == null || actual == null) return false;
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.US_ASCII),
                actual.getBytes(StandardCharsets.US_ASCII)
        );
    }

    private static String normalized(String value) {
        return (value == null ? "" : value).replace("\r\n", "\n").stripTrailing();
    }

    private static String truncate(String value, int maximumCharacters) {
        if (value == null) return "";
        String sanitized = value.replace("\u0000", "");
        return sanitized.length() <= maximumCharacters
                ? sanitized
                : sanitized.substring(0, maximumCharacters) + "\n…output truncated";
    }

    public record Work(
            UUID id,
            String leaseToken,
            CodingLanguage language,
            String sourceCode,
            List<WorkerCase> cases
    ) {}

    public record WorkerCase(int position, String input) {}

    public enum WorkerCaseStatus {
        EXECUTED,
        COMPILE_ERROR,
        RUNTIME_ERROR,
        TIME_LIMIT,
        INTERNAL_ERROR
    }

    public record WorkerCaseResult(
            int position,
            WorkerCaseStatus status,
            String actualOutput,
            String diagnostic,
            String timeSeconds,
            Integer memoryKilobytes
    ) {}

    public record Completion(
            UUID id,
            String email,
            String problemTitle,
            CodingExecutionStatus status,
            int passed,
            int total
    ) {}
}
