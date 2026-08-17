package com.preppilot.api.problem;

import com.preppilot.api.auth.PrepPilotPrincipal;
import com.preppilot.api.common.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    PageResponse<ProblemService.ProblemResponse> findProblems(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page cannot be negative.") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be positive.")
            @Max(value = 50, message = "Page size cannot exceed 50.") int size
    ) {
        UUID userId = principal == null ? null : principal.id();
        return problemService.findProblems(query, difficulty, topic, status, userId, page, size);
    }

    @GetMapping("/topics")
    List<String> findTopics() {
        return problemService.findTopics();
    }
}
