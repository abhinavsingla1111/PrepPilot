package com.preppilot.api.review;

import com.preppilot.api.assessment.AssessmentAttemptQuestion;
import com.preppilot.api.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(
        name = "review_item",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_review_user_question",
                columnNames = {"user_id", "attempt_question_id"}
        )
)
public class ReviewItem {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_question_id", nullable = false)
    private AssessmentAttemptQuestion attemptQuestion;

    @Column(name = "due_at", nullable = false)
    private Instant dueAt;

    @Column(name = "interval_days", nullable = false)
    private int intervalDays;

    @Column(name = "repetition_count", nullable = false)
    private int repetitionCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_rating", length = 20)
    private ReviewRating lastRating;

    @Column(name = "last_reviewed_at")
    private Instant lastReviewedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ReviewItem() {
    }

    public ReviewItem(UUID id, AppUser user, AssessmentAttemptQuestion attemptQuestion, Instant now) {
        this.id = id;
        this.user = user;
        this.attemptQuestion = attemptQuestion;
        this.dueAt = now;
        this.intervalDays = 1;
        this.repetitionCount = 0;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public UUID getId() {
        return id;
    }

    public AssessmentAttemptQuestion getAttemptQuestion() {
        return attemptQuestion;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public int getRepetitionCount() {
        return repetitionCount;
    }

    public ReviewRating getLastRating() {
        return lastRating;
    }

    public Instant getLastReviewedAt() {
        return lastReviewedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isDue(Instant now) {
        return !dueAt.isAfter(now);
    }

    public boolean isMastered() {
        return repetitionCount >= 4;
    }

    public void rate(ReviewRating rating, Instant now) {
        switch (rating) {
            case FORGOT -> {
                repetitionCount = 0;
                intervalDays = 1;
            }
            case DIFFICULT -> {
                repetitionCount = Math.max(1, repetitionCount);
                intervalDays = Math.min(30, Math.max(2, intervalDays + 2));
            }
            case REMEMBERED -> {
                repetitionCount++;
                intervalDays = rememberedInterval(repetitionCount, intervalDays);
            }
            case EASY -> {
                repetitionCount = Math.min(1000, repetitionCount + 2);
                intervalDays = repetitionCount <= 2 ? 7 : repetitionCount <= 4 ? 21 : 60;
            }
        }
        lastRating = rating;
        lastReviewedAt = now;
        dueAt = now.plus(intervalDays, ChronoUnit.DAYS);
        updatedAt = now;
    }

    private static int rememberedInterval(int repetitions, int currentInterval) {
        return switch (repetitions) {
            case 1 -> 3;
            case 2 -> 7;
            case 3 -> 14;
            case 4 -> 30;
            default -> Math.min(90, Math.max(30, currentInterval * 2));
        };
    }
}

