package com.preppilot.api.progress;

import com.preppilot.api.problem.Problem;
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
        name = "user_progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_problem_progress", columnNames = {"user_id", "problem_id"})
)
public class UserProgress {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressStatus status;

    @Column(name = "solved_at")
    private Instant solvedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserProgress() {
    }

    public UserProgress(UUID id, AppUser user, Problem problem, ProgressStatus status, Instant now) {
        this.id = id;
        this.user = user;
        this.problem = problem;
        update(status, now);
    }

    public UUID getId() {
        return id;
    }

    public Problem getProblem() {
        return problem;
    }

    public ProgressStatus getStatus() {
        return status;
    }

    public Instant getSolvedAt() {
        return solvedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update(ProgressStatus newStatus, Instant now) {
        if (newStatus == ProgressStatus.SOLVED && status != ProgressStatus.SOLVED) {
            solvedAt = now;
        } else if (newStatus != ProgressStatus.SOLVED) {
            solvedAt = null;
        }
        status = newStatus;
        updatedAt = now;
    }
}
