package com.preppilot.api.interview;

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
@Table(name = "coding_interview_session")
public class CodingInterviewSession {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prompt_id", nullable = false)
    private CodingInterviewPrompt prompt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingInterviewStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingLanguage language;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "solution_code", nullable = false, columnDefinition = "TEXT")
    private String solutionCode;

    @Column(name = "approach_notes", nullable = false, columnDefinition = "TEXT")
    private String approachNotes;

    @Column(name = "complexity_analysis", nullable = false, columnDefinition = "TEXT")
    private String complexityAnalysis;

    @Column(name = "rubric_clarification")
    private Integer rubricClarification;

    @Column(name = "rubric_approach")
    private Integer rubricApproach;

    @Column(name = "rubric_correctness")
    private Integer rubricCorrectness;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reflection;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CodingInterviewSession() {
    }

    public CodingInterviewSession(
            UUID id,
            AppUser user,
            CodingInterviewPrompt prompt,
            CodingLanguage language,
            Instant startedAt,
            Instant expiresAt
    ) {
        this.id = id;
        this.user = user;
        this.prompt = prompt;
        this.language = language;
        this.status = CodingInterviewStatus.IN_PROGRESS;
        this.startedAt = startedAt;
        this.expiresAt = expiresAt;
        this.solutionCode = prompt.starterFor(language);
        this.approachNotes = "";
        this.complexityAnalysis = "";
        this.reflection = "";
        this.updatedAt = startedAt;
    }

    public UUID getId() { return id; }
    public AppUser getUser() { return user; }
    public CodingInterviewPrompt getPrompt() { return prompt; }
    public CodingInterviewStatus getStatus() { return status; }
    public CodingLanguage getLanguage() { return language; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getSubmittedAt() { return submittedAt; }
    public String getSolutionCode() { return solutionCode; }
    public String getApproachNotes() { return approachNotes; }
    public String getComplexityAnalysis() { return complexityAnalysis; }
    public Integer getRubricClarification() { return rubricClarification; }
    public Integer getRubricApproach() { return rubricApproach; }
    public Integer getRubricCorrectness() { return rubricCorrectness; }
    public String getReflection() { return reflection; }

    public boolean isInProgress() {
        return status == CodingInterviewStatus.IN_PROGRESS;
    }

    public boolean hasExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    public void updateDraft(
            CodingLanguage nextLanguage,
            String nextSolutionCode,
            String nextApproachNotes,
            String nextComplexityAnalysis,
            Instant now
    ) {
        if (!isInProgress()) return;
        language = nextLanguage;
        solutionCode = nextSolutionCode;
        approachNotes = nextApproachNotes;
        complexityAnalysis = nextComplexityAnalysis;
        updatedAt = now;
    }

    public void complete(
            CodingLanguage nextLanguage,
            String nextSolutionCode,
            String nextApproachNotes,
            String nextComplexityAnalysis,
            InterviewRubric rubric,
            String nextReflection,
            Instant now,
            boolean timedOut
    ) {
        if (!isInProgress() && (status != CodingInterviewStatus.TIMED_OUT || rubricClarification != null)) return;
        language = nextLanguage;
        solutionCode = nextSolutionCode;
        approachNotes = nextApproachNotes;
        complexityAnalysis = nextComplexityAnalysis;
        rubricClarification = rubric.clarification();
        rubricApproach = rubric.approach();
        rubricCorrectness = rubric.correctness();
        reflection = nextReflection;
        status = timedOut || status == CodingInterviewStatus.TIMED_OUT
                ? CodingInterviewStatus.TIMED_OUT
                : CodingInterviewStatus.COMPLETED;
        submittedAt = now;
        updatedAt = now;
    }

    public void expire(Instant now) {
        if (!isInProgress()) return;
        status = CodingInterviewStatus.TIMED_OUT;
        submittedAt = now;
        updatedAt = now;
    }

    public Integer rubricTotal() {
        if (rubricClarification == null) return null;
        return rubricClarification + rubricApproach + rubricCorrectness;
    }

    public record InterviewRubric(
            int clarification,
            int approach,
            int correctness
    ) {
    }
}
