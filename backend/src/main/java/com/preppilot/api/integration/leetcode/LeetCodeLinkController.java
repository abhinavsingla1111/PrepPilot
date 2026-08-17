package com.preppilot.api.integration.leetcode;

import com.preppilot.api.auth.PrepPilotPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leetcode/link")
public class LeetCodeLinkController {

    private final LeetCodeAccountService accountService;

    public LeetCodeLinkController(LeetCodeAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    LeetCodeAccountService.LeetCodeStatus status(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return accountService.getStatus(principal);
    }

    @PutMapping
    LeetCodeAccountService.LeetCodeStatus link(
            @AuthenticationPrincipal PrepPilotPrincipal principal,
            @Valid @RequestBody LinkRequest request
    ) {
        return accountService.link(principal, request.username());
    }

    @DeleteMapping
    LeetCodeAccountService.LeetCodeStatus unlink(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return accountService.unlink(principal);
    }

    @PostMapping("/sync")
    LeetCodeAccountService.LeetCodeStatus sync(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return accountService.sync(principal);
    }

    public record LinkRequest(
            @NotBlank(message = "Enter your LeetCode username.")
            @Pattern(regexp = "[A-Za-z0-9_-]{1,30}", message = "Enter a valid LeetCode username.")
            String username
    ) {
    }
}
