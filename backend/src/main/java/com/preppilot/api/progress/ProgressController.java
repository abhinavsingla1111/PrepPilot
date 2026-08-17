package com.preppilot.api.progress;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    List<ProgressService.ProgressResponse> findProgress(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return progressService.findForUser(principal);
    }

    @GetMapping("/summary")
    ProgressService.DashboardSummary summary(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return progressService.getSummary(principal);
    }

    @PutMapping("/{problemId}")
    ProgressService.ProgressResponse updateProgress(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable long problemId,
            @Valid @RequestBody UpdateProgressRequest request
    ) {
        return progressService.update(principal, problemId, request.status());
    }

    public record UpdateProgressRequest(@NotNull(message = "Progress status is required.") ProgressStatus status) {
    }
}
