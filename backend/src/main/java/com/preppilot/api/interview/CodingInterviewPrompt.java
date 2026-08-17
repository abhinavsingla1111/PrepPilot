package com.preppilot.api.interview;

import com.preppilot.api.problem.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coding_interview_prompt")
public class CodingInterviewPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false, length = 180)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Difficulty difficulty;

    @Column(nullable = false, length = 120)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "constraints_text", nullable = false, columnDefinition = "TEXT")
    private String constraintsText;

    @Column(name = "examples_text", nullable = false, columnDefinition = "TEXT")
    private String examplesText;

    @Column(name = "expected_approach", nullable = false, columnDefinition = "TEXT")
    private String expectedApproach;

    @Column(name = "expected_time_complexity", nullable = false, length = 120)
    private String expectedTimeComplexity;

    @Column(name = "expected_space_complexity", nullable = false, length = 120)
    private String expectedSpaceComplexity;

    @Column(name = "learning_order", nullable = false, unique = true)
    private int learningOrder;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "company_tags", nullable = false, length = 300)
    private String companyTags;

    @Column(name = "input_format", nullable = false, columnDefinition = "TEXT")
    private String inputFormat;

    @Column(name = "output_format", nullable = false, columnDefinition = "TEXT")
    private String outputFormat;

    @Column(name = "starter_java", nullable = false, columnDefinition = "TEXT")
    private String starterJava;

    @Column(name = "starter_cpp", nullable = false, columnDefinition = "TEXT")
    private String starterCpp;

    @Column(name = "starter_python", nullable = false, columnDefinition = "TEXT")
    private String starterPython;

    @Column(name = "public_tests_json", nullable = false, columnDefinition = "TEXT")
    private String publicTestsJson;

    @Column(name = "hidden_tests_json", nullable = false, columnDefinition = "TEXT")
    private String hiddenTestsJson;

    protected CodingInterviewPrompt() {
    }

    public CodingInterviewPrompt(String slug, int learningOrder) {
        this.slug = slug;
        this.learningOrder = learningOrder;
        this.title = "";
        this.difficulty = Difficulty.MEDIUM;
        this.topic = "";
        this.prompt = "";
        this.constraintsText = "";
        this.examplesText = "";
        this.expectedApproach = "";
        this.expectedTimeComplexity = "";
        this.expectedSpaceComplexity = "";
        this.companyTags = "";
        this.inputFormat = "";
        this.outputFormat = "";
        this.starterJava = "";
        this.starterCpp = "";
        this.starterPython = "";
        this.publicTestsJson = "[]";
        this.hiddenTestsJson = "[]";
    }

    public void update(SeedPrompt seed) {
        this.title = seed.title();
        this.difficulty = seed.difficulty();
        this.topic = seed.topic();
        this.prompt = seed.description();
        this.constraintsText = seed.constraints();
        this.examplesText = seed.examples();
        this.expectedApproach = seed.expectedApproach();
        this.expectedTimeComplexity = seed.expectedTimeComplexity();
        this.expectedSpaceComplexity = seed.expectedSpaceComplexity();
        this.learningOrder = seed.learningOrder();
        this.active = true;
        this.companyTags = seed.companyTags();
        this.inputFormat = seed.inputFormat();
        this.outputFormat = seed.outputFormat();
        this.starterJava = seed.starterJava();
        this.starterCpp = seed.starterCpp();
        this.starterPython = seed.starterPython();
        this.publicTestsJson = seed.publicTestsJson();
        this.hiddenTestsJson = seed.hiddenTestsJson();
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public Difficulty getDifficulty() { return difficulty; }
    public String getTopic() { return topic; }
    public String getPrompt() { return prompt; }
    public String getConstraintsText() { return constraintsText; }
    public String getExamplesText() { return examplesText; }
    public String getExpectedApproach() { return expectedApproach; }
    public String getExpectedTimeComplexity() { return expectedTimeComplexity; }
    public String getExpectedSpaceComplexity() { return expectedSpaceComplexity; }
    public String getCompanyTags() { return companyTags; }
    public String getInputFormat() { return inputFormat; }
    public String getOutputFormat() { return outputFormat; }
    public String getPublicTestsJson() { return publicTestsJson; }
    public String getHiddenTestsJson() { return hiddenTestsJson; }

    public String starterFor(CodingLanguage language) {
        return switch (language) {
            case JAVA -> starterJava;
            case CPP -> starterCpp;
            case PYTHON -> starterPython;
        };
    }

    public record SeedPrompt(
            String title,
            Difficulty difficulty,
            String topic,
            String description,
            String constraints,
            String examples,
            String expectedApproach,
            String expectedTimeComplexity,
            String expectedSpaceComplexity,
            int learningOrder,
            String companyTags,
            String inputFormat,
            String outputFormat,
            String starterJava,
            String starterCpp,
            String starterPython,
            String publicTestsJson,
            String hiddenTestsJson
    ) {}
}
