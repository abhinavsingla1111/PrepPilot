package com.preppilot.api.assessment;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/api/v1/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @ModelAttribute
    void preventSensitiveCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
    }

    @GetMapping("/topics")
    List<AssessmentService.TopicSummary> topics(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return assessmentService.listTopics(principal);
    }

    @PostMapping("/topics/{topic}/attempts")
    AssessmentService.AttemptDetail start(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable String topic
    ) {
        return assessmentService.startOrResume(principal, topic);
    }

    @GetMapping("/topics/{topic}/history")
    List<AssessmentService.AttemptSummary> history(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable String topic
    ) {
        return assessmentService.history(principal, topic);
    }

    @GetMapping("/attempts/{attemptId}")
    AssessmentService.AttemptDetail attempt(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID attemptId
    ) {
        return assessmentService.findAttempt(principal, attemptId);
    }

    @PutMapping("/attempts/{attemptId}/questions/{questionId}")
    AssessmentService.AnswerSaved answer(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID attemptId,
            @PathVariable UUID questionId,
            @Valid @RequestBody AnswerRequest request
    ) {
        return assessmentService.saveAnswer(principal, attemptId, questionId, request.selectedOption());
    }

    @PostMapping("/attempts/{attemptId}/submit")
    AssessmentService.AttemptDetail submit(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID attemptId
    ) {
        return assessmentService.submit(principal, attemptId);
    }

    public record AnswerRequest(
            @Min(value = 0, message = "Selected option must be between 0 and 3.")
            @Max(value = 3, message = "Selected option must be between 0 and 3.")
            int selectedOption
    ) {
    }
}
