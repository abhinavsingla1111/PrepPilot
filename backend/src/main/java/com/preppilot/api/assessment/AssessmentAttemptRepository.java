package com.preppilot.api.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, UUID> {

    Optional<AssessmentAttempt> findByIdAndUserId(UUID id, UUID userId);

    Optional<AssessmentAttempt> findFirstByUserIdAndTopicAndStatusOrderByStartedAtDesc(
            UUID userId,
            AssessmentTopic topic,
            AssessmentAttemptStatus status
    );

    List<AssessmentAttempt> findAllByUserIdAndTopicOrderByStartedAtDesc(UUID userId, AssessmentTopic topic);

    long countByUserIdAndTopic(UUID userId, AssessmentTopic topic);
}
