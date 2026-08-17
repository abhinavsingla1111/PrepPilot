package com.preppilot.api.feedback;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<FeedbackService.FeedbackResponse> create(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @RequestParam @NotNull FeedbackType type,
            @RequestParam @NotNull FeedbackSeverity severity,
            @RequestParam @NotBlank @Size(max = 120) String subject,
            @RequestParam @NotBlank @Size(max = 4000) String description,
            @RequestParam(required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(service.create(
                principal.id(), type, severity, subject, description, image
        ));
    }
}
