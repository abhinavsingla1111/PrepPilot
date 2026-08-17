package com.preppilot.api.interview.execution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodingExecutionSubmissionRepository extends JpaRepository<CodingExecutionSubmission, UUID> {
    Optional<CodingExecutionSubmission> findByIdAndUserId(UUID id, UUID userId);
    List<CodingExecutionSubmission> findAllBySessionIdAndUserIdOrderByCreatedAtDesc(UUID sessionId, UUID userId);
    Optional<CodingExecutionSubmission> findFirstBySessionIdAndUserIdOrderByCreatedAtDesc(UUID sessionId, UUID userId);
    Optional<CodingExecutionSubmission> findFirstBySessionIdAndUserIdAndStatusInOrderByCreatedAtDesc(
            UUID sessionId, UUID userId, Collection<CodingExecutionStatus> statuses
    );
    Optional<CodingExecutionSubmission> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);
    long countByUserIdAndCreatedAtGreaterThanEqual(UUID userId, Instant since);

    @Query("""
            SELECT submission.session.id AS sessionId,
                   MAX(submission.passedTests) AS passedTests,
                   MAX(submission.totalTests) AS totalTests
            FROM CodingExecutionSubmission submission
            WHERE submission.user.id = :userId
              AND submission.status IN (
                    com.preppilot.api.interview.execution.CodingExecutionStatus.PASSED,
                    com.preppilot.api.interview.execution.CodingExecutionStatus.FAILED
                  )
            GROUP BY submission.session.id
            """)
    List<BestSessionScore> findBestSessionScores(@Param("userId") UUID userId);

    @Query(value = """
            SELECT *
            FROM coding_execution_submission
            WHERE attempt_count < :maxAttempts
              AND (
                    (status = 'QUEUED' AND available_at <= :now)
                    OR (status = 'RUNNING' AND lease_expires_at < :now)
                  )
            ORDER BY created_at ASC
            LIMIT 1
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<CodingExecutionSubmission> claimCandidate(
            @Param("now") Instant now,
            @Param("maxAttempts") int maxAttempts
    );

    @Modifying
    @Query("""
            UPDATE CodingExecutionSubmission submission
            SET submission.status = com.preppilot.api.interview.execution.CodingExecutionStatus.ERROR,
                submission.failureMessage = :message,
                submission.completedAt = :now,
                submission.leaseTokenHash = null,
                submission.leaseExpiresAt = null
            WHERE submission.status = com.preppilot.api.interview.execution.CodingExecutionStatus.RUNNING
              AND submission.leaseExpiresAt < :now
              AND submission.attemptCount >= :maxAttempts
            """)
    int failExhaustedLeases(
            @Param("now") Instant now,
            @Param("maxAttempts") int maxAttempts,
            @Param("message") String message
    );

    interface BestSessionScore {
        UUID getSessionId();
        Integer getPassedTests();
        Integer getTotalTests();
    }
}
