package com.preppilot.api.assessment;

import com.preppilot.api.problem.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.List;

@Entity
@Table(
        name = "assessment_question",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_assessment_question_topic_source",
                columnNames = {"topic", "source_number"}
        )
)
public class AssessmentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AssessmentTopic topic;

    @Column(name = "source_number", nullable = false)
    private int sourceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Difficulty difficulty;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "option_a", nullable = false, columnDefinition = "TEXT")
    private String optionA;

    @Column(name = "option_b", nullable = false, columnDefinition = "TEXT")
    private String optionB;

    @Column(name = "option_c", nullable = false, columnDefinition = "TEXT")
    private String optionC;

    @Column(name = "option_d", nullable = false, columnDefinition = "TEXT")
    private String optionD;

    @Column(name = "correct_option", nullable = false)
    private int correctOption;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justification;

    protected AssessmentQuestion() {
    }

    public AssessmentQuestion(
            AssessmentTopic topic,
            int sourceNumber,
            Difficulty difficulty,
            String prompt,
            List<String> options,
            int correctOption,
            String justification
    ) {
        if (options.size() != 4) throw new IllegalArgumentException("Assessment questions require four options.");
        if (correctOption < 0 || correctOption > 3) throw new IllegalArgumentException("Correct option must be 0-3.");
        this.topic = topic;
        this.sourceNumber = sourceNumber;
        this.difficulty = difficulty;
        this.prompt = prompt;
        this.optionA = options.get(0);
        this.optionB = options.get(1);
        this.optionC = options.get(2);
        this.optionD = options.get(3);
        this.correctOption = correctOption;
        this.justification = justification;
    }

    public Long getId() {
        return id;
    }

    public AssessmentTopic getTopic() {
        return topic;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getOptions() {
        return List.of(optionA, optionB, optionC, optionD);
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public String getJustification() {
        return justification;
    }

    void refreshContent(
            Difficulty difficulty,
            String prompt,
            List<String> options,
            int correctOption,
            String justification
    ) {
        if (options.size() != 4) throw new IllegalArgumentException("Assessment questions require four options.");
        if (correctOption < 0 || correctOption > 3) throw new IllegalArgumentException("Correct option must be 0-3.");
        this.difficulty = difficulty;
        this.prompt = prompt;
        this.optionA = options.get(0);
        this.optionB = options.get(1);
        this.optionC = options.get(2);
        this.optionD = options.get(3);
        this.correctOption = correctOption;
        this.justification = justification;
    }
}
