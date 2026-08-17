package com.preppilot.api.interview.execution;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.interview.CodingLanguage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/coding")
public class CodingExecutionController {

    private final CodingExecutionService service;

    public CodingExecutionController(CodingExecutionService service) {
        this.service = service;
    }

    @ModelAttribute
    void preventSensitiveCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
    }

    @GetMapping("/runner")
    CodingExecutionService.RunnerStatus status(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return service.status(principal);
    }

    @PostMapping("/sessions/{sessionId}/runs")
    @ResponseStatus(HttpStatus.ACCEPTED)
    CodingExecutionService.ExecutionDetail run(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID sessionId,
            @Valid @RequestBody RunRequest request
    ) {
        return service.submit(principal, sessionId, request.language(), request.sourceCode());
    }

    @GetMapping("/sessions/{sessionId}/runs")
    List<CodingExecutionService.ExecutionDetail> history(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID sessionId
    ) {
        return service.history(principal, sessionId);
    }

    @GetMapping("/runs/{submissionId}")
    CodingExecutionService.ExecutionDetail find(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID submissionId
    ) {
        return service.find(principal, submissionId);
    }

    public record RunRequest(
            @NotNull(message = "Choose a coding language.") CodingLanguage language,
            @Size(min = 1, max = 50_000, message = "Source code must be between 1 and 50,000 characters.") String sourceCode
    ) {}
}
