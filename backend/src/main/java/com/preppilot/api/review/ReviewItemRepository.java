package com.preppilot.api.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewItemRepository extends JpaRepository<ReviewItem, UUID> {

    boolean existsByUserIdAndAttemptQuestionId(UUID userId, UUID attemptQuestionId);

    Optional<ReviewItem> findByIdAndUserId(UUID id, UUID userId);

    List<ReviewItem> findAllByUserIdOrderByDueAtAsc(UUID userId);

    List<ReviewItem> findAllByUserIdAndDueAtLessThanEqualOrderByDueAtAsc(UUID userId, Instant dueAt);

    long countByUserId(UUID userId);

    long countByUserIdAndDueAtLessThanEqual(UUID userId, Instant dueAt);

    long countByUserIdAndRepetitionCountGreaterThanEqual(UUID userId, int repetitionCount);
}

