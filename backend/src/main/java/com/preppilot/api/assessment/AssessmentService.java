package com.preppilot.api.assessment;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.problem.Difficulty;
import com.preppilot.api.review.ReviewService;
import com.preppilot.api.user.AppUser;
import com.preppilot.api.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AssessmentService {

    public static final int QUESTIONS_PER_TEST = 20;
    public static final int MAX_ATTEMPTS_PER_TOPIC = 5;

    private static final int EASY_PER_TEST = 7;
    private static final int MEDIUM_PER_TEST = 8;
    private static final int HARD_PER_TEST = 5;

    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final AssessmentAttemptQuestionRepository attemptQuestionRepository;
    private final AppUserRepository userRepository;
    private final SecureRandom secureRandom;
    private final Clock clock;
    private final ReviewService reviewService;

    public AssessmentService(
            AssessmentQuestionRepository questionRepository,
            AssessmentAttemptRepository attemptRepository,
            AssessmentAttemptQuestionRepository attemptQuestionRepository,
            AppUserRepository userRepository,
            SecureRandom secureRandom,
            Clock clock,
            ReviewService reviewService
    ) {
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.attemptQuestionRepository = attemptQuestionRepository;
        this.userRepository = userRepository;
        this.secureRandom = secureRandom;
        this.clock = clock;
        this.reviewService = reviewService;
    }

    @Transactional
    public List<TopicSummary> listTopics(PrepPilotPrincipal principal) {
        Instant now = clock.instant();
        List<TopicSummary> summaries = new ArrayList<>();

        for (AssessmentTopic topic : AssessmentTopic.values()) {
            List<AssessmentAttempt> attempts = attemptRepository
                    .findAllByUserIdAndTopicOrderByStartedAtDesc(principal.id(), topic);
            for (AssessmentAttempt attempt : attempts) completeIfExpired(attempt, now);

            Integer bestScore = attempts.stream()
                    .map(AssessmentAttempt::getScore)
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(null);
            UUID activeAttemptId = attempts.stream()
                    .filter(AssessmentAttempt::isInProgress)
                    .map(AssessmentAttempt::getId)
                    .findFirst()
                    .orElse(null);

            summaries.add(new TopicSummary(
                    topic.getSlug(),
                    topic.getDisplayName(),
                    questionRepository.countByTopic(topic),
                    attempts.size(),
                    MAX_ATTEMPTS_PER_TOPIC,
                    topic.getDurationMinutes(),
                    bestScore,
                    activeAttemptId
            ));
        }

        return summaries;
    }

    @Transactional
    public AttemptDetail startOrResume(PrepPilotPrincipal principal, String topicSlug) {
        AssessmentTopic topic = AssessmentTopic.fromSlug(topicSlug);
        AppUser user = userRepository.findByIdForUpdate(principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in again."));
        Instant now = clock.instant();

        AssessmentAttempt active = attemptRepository
                .findFirstByUserIdAndTopicAndStatusOrderByStartedAtDesc(
                        principal.id(), topic, AssessmentAttemptStatus.IN_PROGRESS
                )
                .orElse(null);
        if (active != null) {
            completeIfExpired(active, now);
            if (active.isInProgress()) return toDetail(active);
        }

        long attemptsUsed = attemptRepository.countByUserIdAndTopic(principal.id(), topic);
        if (attemptsUsed >= MAX_ATTEMPTS_PER_TOPIC) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "You have completed all five " + topic.getDisplayName() + " knowledge checks."
            );
        }

        List<AssessmentQuestion> selected = selectUnusedQuestions(principal.id(), topic);
        AssessmentAttempt attempt = new AssessmentAttempt(
                UUID.randomUUID(),
                user,
                topic,
                (int) attemptsUsed + 1,
                now,
                now.plus(Duration.ofMinutes(topic.getDurationMinutes()))
        );
        attemptRepository.save(attempt);

        List<AssessmentAttemptQuestion> attemptQuestions = new ArrayList<>();
        for (int index = 0; index < selected.size(); index++) {
            attemptQuestions.add(new AssessmentAttemptQuestion(
                    UUID.randomUUID(), attempt, selected.get(index), index + 1
            ));
        }
        attemptQuestionRepository.saveAll(attemptQuestions);
        return toDetail(attempt, attemptQuestions);
    }

    @Transactional
    public AttemptDetail findAttempt(PrepPilotPrincipal principal, UUID attemptId) {
        AssessmentAttempt attempt = requireOwnedAttempt(principal.id(), attemptId);
        completeIfExpired(attempt, clock.instant());
        return toDetail(attempt);
    }

    @Transactional
    public AnswerSaved saveAnswer(
            PrepPilotPrincipal principal,
            UUID attemptId,
            UUID attemptQuestionId,
            int selectedOption
    ) {
        AssessmentAttempt attempt = requireOwnedAttempt(principal.id(), attemptId);
        Instant now = clock.instant();
        if (attempt.hasExpired(now)) {
            completeAttempt(attempt, now, true);
            throw new ApiException(HttpStatus.CONFLICT, "Time is up. Your saved answers were submitted.");
        }
        if (!attempt.isInProgress()) {
            throw new ApiException(HttpStatus.CONFLICT, "This practice test has already been submitted.");
        }

        AssessmentAttemptQuestion question = attemptQuestionRepository
                .findByIdAndAttemptId(attemptQuestionId, attemptId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Question not found in this test."));
        question.select(selectedOption, now);
        attemptQuestionRepository.save(question);

        long answered = attemptQuestionRepository.findAllByAttemptIdOrderByPosition(attemptId).stream()
                .filter(item -> item.getSelectedOption() != null)
                .count();
        return new AnswerSaved(question.getId(), selectedOption, answered);
    }

    @Transactional
    public AttemptDetail submit(PrepPilotPrincipal principal, UUID attemptId) {
        AssessmentAttempt attempt = requireOwnedAttempt(principal.id(), attemptId);
        Instant now = clock.instant();
        if (attempt.isInProgress()) completeAttempt(attempt, now, attempt.hasExpired(now));
        return toDetail(attempt);
    }

    @Transactional
    public List<AttemptSummary> history(PrepPilotPrincipal principal, String topicSlug) {
        AssessmentTopic topic = AssessmentTopic.fromSlug(topicSlug);
        Instant now = clock.instant();
        return attemptRepository.findAllByUserIdAndTopicOrderByStartedAtDesc(principal.id(), topic).stream()
                .peek(attempt -> completeIfExpired(attempt, now))
                .map(AttemptSummary::from)
                .toList();
    }

    private List<AssessmentQuestion> selectUnusedQuestions(UUID userId, AssessmentTopic topic) {
        Set<Long> usedQuestionIds = new HashSet<>(attemptQuestionRepository.findUsedQuestionIds(userId, topic));
        List<AssessmentQuestion> selected = new ArrayList<>(QUESTIONS_PER_TEST);
        selected.addAll(randomUnused(topic, Difficulty.EASY, EASY_PER_TEST, usedQuestionIds));
        selected.addAll(randomUnused(topic, Difficulty.MEDIUM, MEDIUM_PER_TEST, usedQuestionIds));
        selected.addAll(randomUnused(topic, Difficulty.HARD, HARD_PER_TEST, usedQuestionIds));
        Collections.shuffle(selected, secureRandom);
        return selected;
    }

    private List<AssessmentQuestion> randomUnused(
            AssessmentTopic topic,
            Difficulty difficulty,
            int required,
            Set<Long> usedQuestionIds
    ) {
        List<AssessmentQuestion> candidates = questionRepository.findAllByTopicAndDifficulty(topic, difficulty)
                .stream()
                .filter(question -> !usedQuestionIds.contains(question.getId()))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        if (candidates.size() < required) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Not enough unused " + difficulty.name().toLowerCase() + " questions remain for this test."
            );
        }
        Collections.shuffle(candidates, secureRandom);
        return List.copyOf(candidates.subList(0, required));
    }

    private AssessmentAttempt requireOwnedAttempt(UUID userId, UUID attemptId) {
        return attemptRepository.findByIdAndUserId(attemptId, userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Knowledge-check attempt not found."));
    }

    private void completeIfExpired(AssessmentAttempt attempt, Instant now) {
        if (attempt.isInProgress() && attempt.hasExpired(now)) completeAttempt(attempt, now, true);
    }

    private void completeAttempt(AssessmentAttempt attempt, Instant now, boolean timedOut) {
        List<AssessmentAttemptQuestion> questions = attemptQuestionRepository
                .findAllByAttemptIdOrderByPosition(attempt.getId());
        int score = (int) questions.stream().filter(AssessmentAttemptQuestion::isCorrect).count();
        attempt.complete(score, now, timedOut);
        attemptRepository.save(attempt);
        reviewService.addMistakes(attempt.getUser(), questions);
    }

    private AttemptDetail toDetail(AssessmentAttempt attempt) {
        return toDetail(
                attempt,
                attemptQuestionRepository.findAllByAttemptIdOrderByPosition(attempt.getId())
        );
    }

    private AttemptDetail toDetail(AssessmentAttempt attempt, List<AssessmentAttemptQuestion> questions) {
        boolean review = !attempt.isInProgress();
        List<QuestionDetail> questionDetails = questions.stream()
                .sorted(Comparator.comparingInt(AssessmentAttemptQuestion::getPosition))
                .map(question -> QuestionDetail.from(question, review))
                .toList();
        long answered = questions.stream().filter(question -> question.getSelectedOption() != null).count();
        return new AttemptDetail(
                attempt.getId(),
                attempt.getTopic().getSlug(),
                attempt.getTopic().getDisplayName(),
                attempt.getAttemptNumber(),
                attempt.getStatus(),
                attempt.getStartedAt(),
                attempt.getExpiresAt(),
                attempt.getSubmittedAt(),
                attempt.getScore(),
                QUESTIONS_PER_TEST,
                answered,
                questionDetails
        );
    }

    public record TopicSummary(
            String slug,
            String name,
            long totalQuestions,
            long attemptsUsed,
            int maxAttempts,
            int durationMinutes,
            Integer bestScore,
            UUID activeAttemptId
    ) {
    }

    public record AttemptSummary(
            UUID id,
            int attemptNumber,
            AssessmentAttemptStatus status,
            Integer score,
            Instant startedAt,
            Instant submittedAt
    ) {
        static AttemptSummary from(AssessmentAttempt attempt) {
            return new AttemptSummary(
                    attempt.getId(),
                    attempt.getAttemptNumber(),
                    attempt.getStatus(),
                    attempt.getScore(),
                    attempt.getStartedAt(),
                    attempt.getSubmittedAt()
            );
        }
    }

    public record AttemptDetail(
            UUID id,
            String topic,
            String topicName,
            int attemptNumber,
            AssessmentAttemptStatus status,
            Instant startedAt,
            Instant expiresAt,
            Instant submittedAt,
            Integer score,
            int totalQuestions,
            long answeredQuestions,
            List<QuestionDetail> questions
    ) {
    }

    public record QuestionDetail(
            UUID id,
            int position,
            Difficulty difficulty,
            String prompt,
            List<String> options,
            Integer selectedOption,
            Integer correctOption,
            Boolean correct,
            String justification
    ) {
        static QuestionDetail from(AssessmentAttemptQuestion question, boolean review) {
            return new QuestionDetail(
                    question.getId(),
                    question.getPosition(),
                    question.getDifficulty(),
                    question.getPrompt(),
                    question.getOptions(),
                    question.getSelectedOption(),
                    review ? question.getCorrectOption() : null,
                    review ? question.isCorrect() : null,
                    review ? question.getJustification() : null
            );
        }
    }

    public record AnswerSaved(UUID questionId, int selectedOption, long answeredQuestions) {
    }
}
