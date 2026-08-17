package com.preppilot.api.assessment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentAttemptQuestionRepository extends JpaRepository<AssessmentAttemptQuestion, UUID> {

    List<AssessmentAttemptQuestion> findAllByAttemptIdOrderByPosition(UUID attemptId);

    Optional<AssessmentAttemptQuestion> findByIdAndAttemptId(UUID id, UUID attemptId);

    @Query("""
            select aq.question.id
            from AssessmentAttemptQuestion aq
            where aq.attempt.user.id = :userId and aq.attempt.topic = :topic
            """)
    List<Long> findUsedQuestionIds(UUID userId, AssessmentTopic topic);
}
