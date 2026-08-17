package com.preppilot.api.interview.execution;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.interview.CodingInterviewSession;
import com.preppilot.api.interview.CodingInterviewSessionRepository;
import com.preppilot.api.interview.CodingLanguage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
public class CodingExecutionService {

    private static final int MAX_SOURCE_LENGTH = 50_000;
    private static final EnumSet<CodingExecutionStatus> ACTIVE = EnumSet.of(
            CodingExecutionStatus.QUEUED, CodingExecutionStatus.RUNNING
    );

    private final CodingExecutionSubmissionRepository repository;
    private final CodingInterviewSessionRepository sessionRepository;
    private final CodingTestCaseParser testCaseParser;
    private final RunnerProperties properties;
    private final CodingExecutionStore store;
    private final Clock clock;

    public CodingExecutionService(
            CodingExecutionSubmissionRepository repository,
            CodingInterviewSessionRepository sessionRepository,
            CodingTestCaseParser testCaseParser,
            RunnerProperties properties,
            CodingExecutionStore store,
            Clock clock
    ) {
        this.repository = repository;
        this.sessionRepository = sessionRepository;
        this.testCaseParser = testCaseParser;
        this.properties = properties;
        this.store = store;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public RunnerStatus status(PrepPilotPrincipal principal) {
        long used = repository.countByUserIdAndCreatedAtGreaterThanEqual(
                principal.id(), clock.instant().minus(Duration.ofHours(24))
        );
        return new RunnerStatus(
                properties.enabled(), properties.emailResults(), properties.dailyRunLimit(),
                Math.max(0, properties.dailyRunLimit() - used)
        );
    }

    @Transactional
    public ExecutionDetail submit(PrepPilotPrincipal principal, UUID sessionId, CodingLanguage language, String sourceCode) {
        if (!properties.enabled()) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Code execution is not configured on this deployment yet.");
        }
        String code = sourceCode == null ? "" : sourceCode;
        if (code.isBlank() || code.length() > MAX_SOURCE_LENGTH) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Provide between 1 and 50,000 characters of source code.");
        }

        CodingInterviewSession session = sessionRepository.findByIdAndUserIdForUpdate(sessionId, principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Coding interview session not found."));
        Instant now = clock.instant();
        if (!session.isInProgress() || session.hasExpired(now)) {
            throw new ApiException(HttpStatus.CONFLICT, "This interview is no longer open for code runs.");
        }
        if (repository.findFirstBySessionIdAndUserIdAndStatusInOrderByCreatedAtDesc(
                sessionId, principal.id(), ACTIVE).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "A code run is already in progress for this interview.");
        }

        long used = repository.countByUserIdAndCreatedAtGreaterThanEqual(principal.id(), now.minus(Duration.ofHours(24)));
        if (used >= properties.dailyRunLimit()) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS, "Daily code-run limit reached. Try again later.");
        }
        repository.findFirstByUserIdOrderByCreatedAtDesc(principal.id()).ifPresent(latest -> {
            if (latest.getCreatedAt().plus(properties.minimumRunInterval()).isAfter(now)) {
                throw new ApiException(HttpStatus.TOO_MANY_REQUESTS, "Please wait a moment before running code again.");
            }
        });

        List<CodeExecutionGateway.ExecutionCase> cases = testCaseParser.parse(session.getPrompt());
        session.updateDraft(language, code, session.getApproachNotes(), session.getComplexityAnalysis(), now);
        sessionRepository.save(session);
        CodingExecutionSubmission submission = repository.save(new CodingExecutionSubmission(
                UUID.randomUUID(), session.getUser(), session, language, code, cases.size(), now
        ));
        return detail(submission);
    }

    @Transactional(readOnly = true)
    public List<ExecutionDetail> history(PrepPilotPrincipal principal, UUID sessionId) {
        requireOwnedSession(principal.id(), sessionId);
        return repository.findAllBySessionIdAndUserIdOrderByCreatedAtDesc(sessionId, principal.id())
                .stream().map(this::detail).toList();
    }

    @Transactional(readOnly = true)
    public ExecutionDetail find(PrepPilotPrincipal principal, UUID submissionId) {
        return detail(repository.findByIdAndUserId(submissionId, principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Code run not found.")));
    }

    private void requireOwnedSession(UUID userId, UUID sessionId) {
        if (sessionRepository.findByIdAndUserId(sessionId, userId).isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Coding interview session not found.");
        }
    }

    private ExecutionDetail detail(CodingExecutionSubmission submission) {
        List<CodeExecutionGateway.CaseOutcome> outcomes = submission.getResultsJson().equals("[]")
                ? List.of() : store.readOutcomes(submission);
        return new ExecutionDetail(
                submission.getId(), submission.getSession().getId(), submission.getStatus(), submission.getLanguage(),
                submission.getTotalTests(), submission.getPassedTests(), outcomes, submission.getFailureMessage(),
                submission.getCreatedAt(), submission.getStartedAt(), submission.getCompletedAt()
        );
    }

    public record RunnerStatus(boolean enabled, boolean emailResults, int dailyLimit, long remainingToday) {}

    public record ExecutionDetail(
            UUID id,
            UUID sessionId,
            CodingExecutionStatus status,
            CodingLanguage language,
            int totalTests,
            int passedTests,
            List<CodeExecutionGateway.CaseOutcome> results,
            String failureMessage,
            Instant createdAt,
            Instant startedAt,
            Instant completedAt
    ) {}
}
