package com.preppilot.api.assessment;

import com.preppilot.api.problem.Difficulty;
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
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "assessment_attempt_question",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_attempt_question_bank", columnNames = {"attempt_id", "question_id"}),
                @UniqueConstraint(name = "uk_attempt_question_position", columnNames = {"attempt_id", "position"})
        }
)
public class AssessmentAttemptQuestion {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestion question;

    @Column(nullable = false)
    private int position;

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

    @Column(name = "selected_option")
    private Integer selectedOption;

    @Column(name = "answered_at")
    private Instant answeredAt;

    protected AssessmentAttemptQuestion() {
    }

    public AssessmentAttemptQuestion(UUID id, AssessmentAttempt attempt, AssessmentQuestion question, int position) {
        List<String> options = question.getOptions();
        this.id = id;
        this.attempt = attempt;
        this.question = question;
        this.position = position;
        this.difficulty = question.getDifficulty();
        this.prompt = question.getPrompt();
        this.optionA = options.get(0);
        this.optionB = options.get(1);
        this.optionC = options.get(2);
        this.optionD = options.get(3);
        this.correctOption = question.getCorrectOption();
        this.justification = question.getJustification();
    }

    public UUID getId() {
        return id;
    }

    public AssessmentAttempt getAttempt() {
        return attempt;
    }

    public AssessmentQuestion getQuestion() {
        return question;
    }

    public int getPosition() {
        return position;
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

    public Integer getSelectedOption() {
        return selectedOption;
    }

    public boolean isCorrect() {
        return selectedOption != null && selectedOption == correctOption;
    }

    public void select(int option, Instant now) {
        if (option < 0 || option > 3) throw new IllegalArgumentException("Selected option must be 0-3.");
        this.selectedOption = option;
        this.answeredAt = now;
    }
}
