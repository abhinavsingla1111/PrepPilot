package com.preppilot.api.review;

import com.preppilot.api.assessment.AssessmentAttemptQuestion;
import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.ApiException;
import com.preppilot.api.user.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private static final int MASTERY_REPETITIONS = 4;

    private final ReviewItemRepository reviewRepository;
    private final Clock clock;

    public ReviewService(ReviewItemRepository reviewRepository, Clock clock) {
        this.reviewRepository = reviewRepository;
        this.clock = clock;
    }

    @Transactional
    public void addMistakes(AppUser user, List<AssessmentAttemptQuestion> questions) {
        Instant now = clock.instant();
        List<ReviewItem> missing = questions.stream()
                .filter(question -> !question.isCorrect())
                .filter(question -> !reviewRepository.existsByUserIdAndAttemptQuestionId(user.getId(), question.getId()))
                .map(question -> new ReviewItem(UUID.randomUUID(), user, question, now))
                .toList();
        reviewRepository.saveAll(missing);
    }

    @Transactional(readOnly = true)
    public ReviewSummary summary(PrepPilotPrincipal principal) {
        Instant now = clock.instant();
        return new ReviewSummary(
                reviewRepository.countByUserIdAndDueAtLessThanEqual(principal.id(), now),
                reviewRepository.countByUserId(principal.id()),
                reviewRepository.countByUserIdAndRepetitionCountGreaterThanEqual(principal.id(), MASTERY_REPETITIONS)
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewDetail> list(PrepPilotPrincipal principal, boolean dueOnly) {
        Instant now = clock.instant();
        List<ReviewItem> items = dueOnly
                ? reviewRepository.findAllByUserIdAndDueAtLessThanEqualOrderByDueAtAsc(principal.id(), now)
                : reviewRepository.findAllByUserIdOrderByDueAtAsc(principal.id());
        return items.stream().map(item -> ReviewDetail.from(item, now)).toList();
    }

    @Transactional
    public ReviewDetail rate(PrepPilotPrincipal principal, UUID reviewId, ReviewRating rating) {
        ReviewItem item = reviewRepository.findByIdAndUserId(reviewId, principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Review item not found."));
        Instant now = clock.instant();
        item.rate(rating, now);
        return ReviewDetail.from(reviewRepository.save(item), now);
    }

    public record ReviewSummary(long due, long total, long mastered) {
    }

    public record ReviewDetail(
            UUID id,
            String topic,
            String topicName,
            String prompt,
            List<String> options,
            Integer originalAnswer,
            int correctOption,
            String justification,
            Instant dueAt,
            int intervalDays,
            int repetitionCount,
            ReviewRating lastRating,
            Instant lastReviewedAt,
            boolean due,
            boolean mastered
    ) {
        static ReviewDetail from(ReviewItem item, Instant now) {
            AssessmentAttemptQuestion question = item.getAttemptQuestion();
            return new ReviewDetail(
                    item.getId(),
                    question.getAttempt().getTopic().getSlug(),
                    question.getAttempt().getTopic().getDisplayName(),
                    question.getPrompt(),
                    question.getOptions(),
                    question.getSelectedOption(),
                    question.getCorrectOption(),
                    question.getJustification(),
                    item.getDueAt(),
                    item.getIntervalDays(),
                    item.getRepetitionCount(),
                    item.getLastRating(),
                    item.getLastReviewedAt(),
                    item.isDue(now),
                    item.isMastered()
            );
        }
    }
}

