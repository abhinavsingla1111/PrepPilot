package com.preppilot.api.assessment;

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
import java.util.UUID;

@Entity
@Table(
        name = "assessment_attempt",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_assessment_attempt_user_topic_number",
                columnNames = {"user_id", "topic", "attempt_number"}
        )
)
public class AssessmentAttempt {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AssessmentTopic topic;

    @Column(name = "attempt_number", nullable = false)
    private int attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssessmentAttemptStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column
    private Integer score;

    protected AssessmentAttempt() {
    }

    public AssessmentAttempt(
            UUID id,
            AppUser user,
            AssessmentTopic topic,
            int attemptNumber,
            Instant startedAt,
            Instant expiresAt
    ) {
        this.id = id;
        this.user = user;
        this.topic = topic;
        this.attemptNumber = attemptNumber;
        this.status = AssessmentAttemptStatus.IN_PROGRESS;
        this.startedAt = startedAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public AssessmentTopic getTopic() {
        return topic;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public AssessmentAttemptStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public Integer getScore() {
        return score;
    }

    public boolean isInProgress() {
        return status == AssessmentAttemptStatus.IN_PROGRESS;
    }

    public boolean hasExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    public void complete(int finalScore, Instant now, boolean timedOut) {
        if (!isInProgress()) return;
        this.score = finalScore;
        this.submittedAt = now;
        this.status = timedOut ? AssessmentAttemptStatus.TIMED_OUT : AssessmentAttemptStatus.COMPLETED;
    }
}
