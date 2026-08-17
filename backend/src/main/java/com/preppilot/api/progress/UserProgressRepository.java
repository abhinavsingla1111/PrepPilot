package com.preppilot.api.progress;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProgressRepository extends JpaRepository<UserProgress, UUID> {

    @EntityGraph(attributePaths = "problem")
    List<UserProgress> findAllByUserIdOrderByUpdatedAtDesc(UUID userId);

    Optional<UserProgress> findByUserIdAndProblemId(UUID userId, Long problemId);

    long countByUserIdAndStatus(UUID userId, ProgressStatus status);

    long countByUserIdAndStatusAndSolvedAtAfter(UUID userId, ProgressStatus status, Instant after);

    @Query("select up.problem.id from UserProgress up where up.user.id = :userId and up.status = :status")
    List<Long> findProblemIdsByStatus(UUID userId, ProgressStatus status);
}
