package com.preppilot.api.interview;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/coding")
public class CodingInterviewController {

    private final CodingInterviewService interviewService;

    public CodingInterviewController(CodingInterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @ModelAttribute
    void preventSensitiveCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
    }

    @PostMapping("/sessions")
    CodingInterviewService.InterviewDetail start(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @Valid @RequestBody StartInterviewRequest request
    ) {
        return interviewService.startOrResume(principal, request.language());
    }

    @GetMapping("/sessions")
    List<CodingInterviewService.InterviewSummary> history(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return interviewService.history(principal);
    }

    @GetMapping("/sessions/{sessionId}")
    CodingInterviewService.InterviewDetail find(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID sessionId
    ) {
        return interviewService.find(principal, sessionId);
    }

    @PutMapping("/sessions/{sessionId}/draft")
    CodingInterviewService.InterviewDetail saveDraft(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID sessionId,
            @Valid @RequestBody DraftRequest request
    ) {
        return interviewService.saveDraft(principal, sessionId, request.toService());
    }

    @PostMapping("/sessions/{sessionId}/submit")
    CodingInterviewService.InterviewDetail submit(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID sessionId,
            @Valid @RequestBody SubmitRequest request
    ) {
        return interviewService.submit(principal, sessionId, request.toService());
    }

    public record StartInterviewRequest(
            @NotNull(message = "Choose a coding language.") CodingLanguage language
    ) {
    }

    public record DraftRequest(
            @NotNull(message = "Choose a coding language.") CodingLanguage language,
            @Size(max = 50_000, message = "Solution code must be 50,000 characters or fewer.") String solutionCode,
            @Size(max = 5_000, message = "Approach notes must be 5,000 characters or fewer.") String approachNotes,
            @Size(max = 2_000, message = "Complexity analysis must be 2,000 characters or fewer.") String complexityAnalysis
    ) {
        CodingInterviewService.Draft toService() {
            return new CodingInterviewService.Draft(
                    language, safe(solutionCode), safe(approachNotes), safe(complexityAnalysis)
            );
        }
    }

    public record SubmitRequest(
            @NotNull(message = "Choose a coding language.") CodingLanguage language,
            @Size(max = 50_000, message = "Solution code must be 50,000 characters or fewer.") String solutionCode,
            @Size(max = 5_000, message = "Approach notes must be 5,000 characters or fewer.") String approachNotes,
            @Size(max = 2_000, message = "Complexity analysis must be 2,000 characters or fewer.") String complexityAnalysis,
            @Min(value = 1, message = "Rubric scores must be between 1 and 5.") @Max(value = 5, message = "Rubric scores must be between 1 and 5.") int clarification,
            @Min(value = 1, message = "Rubric scores must be between 1 and 5.") @Max(value = 5, message = "Rubric scores must be between 1 and 5.") int approach,
            @Min(value = 1, message = "Rubric scores must be between 1 and 5.") @Max(value = 5, message = "Rubric scores must be between 1 and 5.") int correctness,
            @Size(max = 5_000, message = "Reflection must be 5,000 characters or fewer.") String reflection
    ) {
        CodingInterviewService.Submission toService() {
            return new CodingInterviewService.Submission(
                    language, safe(solutionCode), safe(approachNotes), safe(complexityAnalysis),
                    clarification, approach, correctness, safe(reflection)
            );
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
