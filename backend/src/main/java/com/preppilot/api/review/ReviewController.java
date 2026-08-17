package com.preppilot.api.review;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @ModelAttribute
    void preventSensitiveCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
    }

    @GetMapping("/summary")
    ReviewService.ReviewSummary summary(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return reviewService.summary(principal);
    }

    @GetMapping
    List<ReviewService.ReviewDetail> list(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @RequestParam(defaultValue = "true") boolean dueOnly
    ) {
        return reviewService.list(principal, dueOnly);
    }

    @PostMapping("/{reviewId}/rating")
    ReviewService.ReviewDetail rate(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @PathVariable UUID reviewId,
            @Valid @RequestBody RateReviewRequest request
    ) {
        return reviewService.rate(principal, reviewId, request.rating());
    }

    public record RateReviewRequest(@NotNull(message = "A review rating is required.") ReviewRating rating) {
    }
}

