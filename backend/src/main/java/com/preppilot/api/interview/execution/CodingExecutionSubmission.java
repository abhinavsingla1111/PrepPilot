package com.preppilot.api.interview.execution;

import com.preppilot.api.interview.CodingInterviewSession;
import com.preppilot.api.interview.CodingLanguage;
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

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "coding_execution_submission")
public class CodingExecutionSubmission {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private CodingInterviewSession session;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingExecutionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingLanguage language;

    @Column(name = "source_code", nullable = false, columnDefinition = "TEXT")
    private String sourceCode;

    @Column(name = "total_tests", nullable = false)
    private int totalTests;

    @Column(name = "passed_tests", nullable = false)
    private int passedTests;

    @Column(name = "results_json", nullable = false, columnDefinition = "TEXT")
    private String resultsJson;

    @Column(name = "failure_message", length = 500)
    private String failureMessage;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "available_at", nullable = false)
    private Instant availableAt;

    @Column(name = "lease_token_hash", length = 64)
    private String leaseTokenHash;

    @Column(name = "lease_expires_at")
    private Instant leaseExpiresAt;

    protected CodingExecutionSubmission() {}

    public CodingExecutionSubmission(
            UUID id,
            AppUser user,
            CodingInterviewSession session,
            CodingLanguage language,
            String sourceCode,
            int totalTests,
            Instant createdAt
    ) {
        this.id = id;
        this.user = user;
        this.session = session;
        this.status = CodingExecutionStatus.QUEUED;
        this.language = language;
        this.sourceCode = sourceCode;
        this.totalTests = totalTests;
        this.passedTests = 0;
        this.resultsJson = "[]";
        this.createdAt = createdAt;
        this.availableAt = createdAt;
    }

    public UUID getId() { return id; }
    public AppUser getUser() { return user; }
    public CodingInterviewSession getSession() { return session; }
    public CodingExecutionStatus getStatus() { return status; }
    public CodingLanguage getLanguage() { return language; }
    public String getSourceCode() { return sourceCode; }
    public int getTotalTests() { return totalTests; }
    public int getPassedTests() { return passedTests; }
    public String getResultsJson() { return resultsJson; }
    public String getFailureMessage() { return failureMessage; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public int getAttemptCount() { return attemptCount; }
    public Instant getAvailableAt() { return availableAt; }
    public String getLeaseTokenHash() { return leaseTokenHash; }
    public Instant getLeaseExpiresAt() { return leaseExpiresAt; }

    public void claim(String tokenHash, Instant now, Instant leaseExpiry) {
        if (status != CodingExecutionStatus.QUEUED && status != CodingExecutionStatus.RUNNING) return;
        status = CodingExecutionStatus.RUNNING;
        if (startedAt == null) startedAt = now;
        attemptCount++;
        leaseTokenHash = tokenHash;
        leaseExpiresAt = leaseExpiry;
    }

    public void complete(int passed, String results, Instant now) {
        if (status != CodingExecutionStatus.RUNNING) return;
        passedTests = passed;
        resultsJson = results;
        status = passed == totalTests ? CodingExecutionStatus.PASSED : CodingExecutionStatus.FAILED;
        completedAt = now;
        clearLease();
    }

    public void fail(String message, Instant now) {
        if (status != CodingExecutionStatus.QUEUED && status != CodingExecutionStatus.RUNNING) return;
        status = CodingExecutionStatus.ERROR;
        failureMessage = message;
        completedAt = now;
        clearLease();
    }

    private void clearLease() {
        leaseTokenHash = null;
        leaseExpiresAt = null;
    }
}
