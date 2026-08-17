package com.preppilot.api.integration.leetcode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/leetcode")
public class LeetCodeController {

    private final LeetCodeSyncService leetCodeSyncService;

    public LeetCodeController(LeetCodeSyncService leetCodeSyncService) {
        this.leetCodeSyncService = leetCodeSyncService;
    }

    @GetMapping("/{username}/recent")
    List<LeetCodeSyncService.RecentAcceptedResponse> findRecentAccepted(
            @PathVariable
            @Pattern(regexp = "[A-Za-z0-9_-]{1,30}", message = "Enter a valid LeetCode username.")
            String username,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int limit
    ) {
        return leetCodeSyncService.findRecentAccepted(username, limit);
    }
}
