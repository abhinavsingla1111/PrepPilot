package com.preppilot.api.interview;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.problem.Difficulty;
import com.preppilot.api.interview.execution.CodingTestCaseParser;
import com.preppilot.api.interview.execution.CodingExecutionSubmissionRepository;
import com.preppilot.api.user.AppUser;
import com.preppilot.api.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CodingInterviewService {

    public static final int DURATION_SECONDS = 45 * 60;

    private final CodingInterviewSessionRepository sessionRepository;
    private final CodingInterviewPromptRepository promptRepository;
    private final AppUserRepository userRepository;
    private final SecureRandom secureRandom;
    private final Clock clock;
    private final CodingTestCaseParser testCaseParser;
    private final CodingExecutionSubmissionRepository executionRepository;

    public CodingInterviewService(
            CodingInterviewSessionRepository sessionRepository,
            CodingInterviewPromptRepository promptRepository,
            AppUserRepository userRepository,
            SecureRandom secureRandom,
            Clock clock,
            CodingTestCaseParser testCaseParser,
            CodingExecutionSubmissionRepository executionRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.promptRepository = promptRepository;
        this.userRepository = userRepository;
        this.secureRandom = secureRandom;
        this.clock = clock;
        this.testCaseParser = testCaseParser;
        this.executionRepository = executionRepository;
    }

    @Transactional
    public InterviewDetail startOrResume(PrepPilotPrincipal principal, CodingLanguage language) {
        AppUser user = userRepository.findByIdForUpdate(principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in again."));
        Instant now = clock.instant();
        CodingInterviewSession active = sessionRepository
                .findFirstByUserIdAndStatusOrderByStartedAtDesc(principal.id(), CodingInterviewStatus.IN_PROGRESS)
                .orElse(null);
        if (active != null) {
            if (active.hasExpired(now)) {
                active.expire(now);
                sessionRepository.save(active);
            } else {
                return InterviewDetail.from(active, testCaseParser);
            }
        }

        List<Long> usedPromptIds = sessionRepository.findUsedPromptIds(principal.id());
        List<CodingInterviewPrompt> candidates = usedPromptIds.isEmpty()
                ? promptRepository.findAllByActiveTrueOrderByLearningOrderAsc()
                : promptRepository.findAllByActiveTrueAndIdNotInOrderByLearningOrderAsc(usedPromptIds);
        if (candidates.isEmpty()) candidates = promptRepository.findAllByActiveTrueOrderByLearningOrderAsc();
        if (candidates.isEmpty()) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Coding interview prompts are not ready yet.");
        }

        CodingInterviewPrompt prompt = candidates.get(secureRandom.nextInt(candidates.size()));
        CodingInterviewSession session = new CodingInterviewSession(
                UUID.randomUUID(), user, prompt, language, now, now.plus(Duration.ofSeconds(DURATION_SECONDS))
        );
        return InterviewDetail.from(sessionRepository.save(session), testCaseParser);
    }

    @Transactional
    public InterviewDetail find(PrepPilotPrincipal principal, UUID sessionId) {
        CodingInterviewSession session = requireOwned(principal.id(), sessionId);
        expireIfRequired(session, clock.instant());
        return InterviewDetail.from(session, testCaseParser);
    }

    @Transactional
    public InterviewDetail saveDraft(PrepPilotPrincipal principal, UUID sessionId, Draft draft) {
        CodingInterviewSession session = requireOwned(principal.id(), sessionId);
        Instant now = clock.instant();
        if (session.hasExpired(now)) {
            session.expire(now);
            sessionRepository.save(session);
            throw new ApiException(HttpStatus.CONFLICT, "Time is up. Your latest saved work is available in review.");
        }
        if (!session.isInProgress()) {
            throw new ApiException(HttpStatus.CONFLICT, "This coding interview has already finished.");
        }
        session.updateDraft(
                draft.language(), draft.solutionCode(), draft.approachNotes(),
                draft.complexityAnalysis(), now
        );
        return InterviewDetail.from(sessionRepository.save(session), testCaseParser);
    }

    @Transactional
    public InterviewDetail submit(PrepPilotPrincipal principal, UUID sessionId, Submission submission) {
        CodingInterviewSession session = requireOwned(principal.id(), sessionId);
        if (!session.isInProgress()
                && (session.getStatus() != CodingInterviewStatus.TIMED_OUT || session.rubricTotal() != null)) {
            return InterviewDetail.from(session, testCaseParser);
        }
        Instant now = clock.instant();
        CodingInterviewSession.InterviewRubric rubric = new CodingInterviewSession.InterviewRubric(
                submission.clarification(), submission.approach(), submission.correctness()
        );
        session.complete(
                submission.language(), submission.solutionCode(), submission.approachNotes(),
                submission.complexityAnalysis(), rubric,
                submission.reflection(), now,
                session.getStatus() == CodingInterviewStatus.TIMED_OUT || session.hasExpired(now)
        );
        return InterviewDetail.from(sessionRepository.save(session), testCaseParser);
    }

    @Transactional
    public List<InterviewSummary> history(PrepPilotPrincipal principal) {
        Instant now = clock.instant();
        List<CodingInterviewSession> sessions = sessionRepository.findAllByUserIdOrderByStartedAtDesc(principal.id());
        sessions.forEach(session -> expireIfRequired(session, now));
        Map<UUID, CodingExecutionSubmissionRepository.BestSessionScore> scores = executionRepository
                .findBestSessionScores(principal.id())
                .stream()
                .collect(Collectors.toMap(
                        CodingExecutionSubmissionRepository.BestSessionScore::getSessionId,
                        Function.identity()
                ));
        return sessions.stream().map(session -> InterviewSummary.from(session, scores.get(session.getId()))).toList();
    }

    private CodingInterviewSession requireOwned(UUID userId, UUID sessionId) {
        return sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Coding interview session not found."));
    }

    private void expireIfRequired(CodingInterviewSession session, Instant now) {
        if (session.isInProgress() && session.hasExpired(now)) {
            session.expire(now);
            sessionRepository.save(session);
        }
    }

    public record Draft(
            CodingLanguage language,
            String solutionCode,
            String approachNotes,
            String complexityAnalysis
    ) {
    }

    public record Submission(
            CodingLanguage language,
            String solutionCode,
            String approachNotes,
            String complexityAnalysis,
            int clarification,
            int approach,
            int correctness,
            String reflection
    ) {
    }

    public record InterviewSummary(
            UUID id,
            String title,
            Difficulty difficulty,
            String topic,
            CodingInterviewStatus status,
            CodingLanguage language,
            Instant startedAt,
            Instant submittedAt,
            Integer bestPassedTests,
            Integer totalTests
    ) {
        static InterviewSummary from(
                CodingInterviewSession session,
                CodingExecutionSubmissionRepository.BestSessionScore score
        ) {
            return new InterviewSummary(
                    session.getId(), session.getPrompt().getTitle(), session.getPrompt().getDifficulty(),
                    session.getPrompt().getTopic(), session.getStatus(), session.getLanguage(),
                    session.getStartedAt(), session.getSubmittedAt(),
                    score == null ? null : score.getPassedTests(),
                    score == null ? null : score.getTotalTests()
            );
        }
    }

    public record InterviewDetail(
            UUID id,
            CodingInterviewStatus status,
            CodingLanguage language,
            Instant startedAt,
            Instant expiresAt,
            Instant submittedAt,
            PromptDetail prompt,
            String solutionCode,
            String approachNotes,
            String complexityAnalysis,
            RubricDetail rubric,
            String reflection
    ) {
        static InterviewDetail from(CodingInterviewSession session, CodingTestCaseParser parser) {
            boolean review = !session.isInProgress();
            return new InterviewDetail(
                    session.getId(), session.getStatus(), session.getLanguage(), session.getStartedAt(),
                    session.getExpiresAt(), session.getSubmittedAt(), PromptDetail.from(session.getPrompt(), review, parser),
                    session.getSolutionCode(), session.getApproachNotes(), session.getComplexityAnalysis(),
                    review ? RubricDetail.from(session) : null,
                    review ? session.getReflection() : ""
            );
        }
    }

    public record PromptDetail(
            String slug,
            String title,
            Difficulty difficulty,
            String topic,
            String companyTags,
            String prompt,
            String constraints,
            String examples,
            String inputFormat,
            String outputFormat,
            Map<CodingLanguage, String> starterCode,
            List<CodingTestCaseParser.PublicExample> sampleTests,
            int totalTests,
            String expectedApproach,
            String expectedTimeComplexity,
            String expectedSpaceComplexity
    ) {
        static PromptDetail from(CodingInterviewPrompt prompt, boolean review, CodingTestCaseParser parser) {
            List<CodingTestCaseParser.PublicExample> samples = parser.publicExamples(prompt);
            int totalTests = parser.parse(prompt).size();
            return new PromptDetail(
                    prompt.getSlug(), prompt.getTitle(), prompt.getDifficulty(), prompt.getTopic(),
                    prompt.getCompanyTags(), prompt.getPrompt(), prompt.getConstraintsText(), prompt.getExamplesText(),
                    prompt.getInputFormat(), prompt.getOutputFormat(),
                    Map.of(
                            CodingLanguage.JAVA, prompt.starterFor(CodingLanguage.JAVA),
                            CodingLanguage.CPP, prompt.starterFor(CodingLanguage.CPP),
                            CodingLanguage.PYTHON, prompt.starterFor(CodingLanguage.PYTHON)
                    ), samples, totalTests,
                    review ? prompt.getExpectedApproach() : null,
                    review ? prompt.getExpectedTimeComplexity() : null,
                    review ? prompt.getExpectedSpaceComplexity() : null
            );
        }
    }

    public record RubricDetail(
            Integer clarification,
            Integer approach,
            Integer correctness,
            Integer total
    ) {
        static RubricDetail from(CodingInterviewSession session) {
            return new RubricDetail(
                    session.getRubricClarification(), session.getRubricApproach(), session.getRubricCorrectness(),
                    session.rubricTotal()
            );
        }
    }
}
