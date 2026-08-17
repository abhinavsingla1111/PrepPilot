package com.preppilot.api.assessment;

import com.preppilot.api.problem.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {

    Optional<AssessmentQuestion> findByTopicAndSourceNumber(AssessmentTopic topic, int sourceNumber);

    List<AssessmentQuestion> findAllByTopicAndDifficulty(AssessmentTopic topic, Difficulty difficulty);

    long countByTopic(AssessmentTopic topic);
}
