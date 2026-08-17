package com.preppilot.api.integration.leetcode;

import com.preppilot.api.common.ApiException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class LeetCodeSyncService {

    private static final String RECENT_ACCEPTED_QUERY = """
            query recentAcSubmissions($username: String!, $limit: Int!) {
              recentAcSubmissionList(username: $username, limit: $limit) {
                title
                titleSlug
                timestamp
              }
            }
            """;

    private static final String SOLVED_STATS_QUERY = """
            query userProblemsSolved($username: String!) {
              matchedUser(username: $username) {
                submitStatsGlobal {
                  acSubmissionNum {
                    difficulty
                    count
                  }
                }
              }
            }
            """;

    private final LeetCodeClient leetCodeClient;

    public LeetCodeSyncService(LeetCodeClient leetCodeClient) {
        this.leetCodeClient = leetCodeClient;
    }

    public SolvedStats getSolvedStats(String username) {
        try {
            LeetCodeClient.GraphQlResponse response = leetCodeClient.fetchUserStats(
                    new LeetCodeClient.GraphQlRequest(
                            SOLVED_STATS_QUERY,
                            Map.of("username", username)
                    )
            );
            if (response.errors() != null && !response.errors().isEmpty()) {
                throw new ApiException(HttpStatus.NOT_FOUND, "We couldn't find a public LeetCode profile for that username.");
            }
            LeetCodeClient.GraphQlData data = response.data();
            if (data == null || data.matchedUser() == null
                    || data.matchedUser().submitStatsGlobal() == null
                    || data.matchedUser().submitStatsGlobal().acSubmissionNum() == null) {
                throw new ApiException(HttpStatus.NOT_FOUND, "We couldn't find a public LeetCode profile for that username.");
            }

            int total = 0;
            int easy = 0;
            int medium = 0;
            int hard = 0;
            for (LeetCodeClient.AcSubmissionNum entry : data.matchedUser().submitStatsGlobal().acSubmissionNum()) {
                switch (entry.difficulty()) {
                    case "All" -> total = entry.count();
                    case "Easy" -> easy = entry.count();
                    case "Medium" -> medium = entry.count();
                    case "Hard" -> hard = entry.count();
                    default -> {
                        // ignore unknown buckets
                    }
                }
            }
            return new SolvedStats(total, easy, medium, hard);
        } catch (FeignException exception) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "LeetCode is temporarily unavailable. Try again shortly.");
        }
    }

    public List<RecentAcceptedResponse> findRecentAccepted(String username, int limit) {
        try {
            LeetCodeClient.GraphQlResponse response = leetCodeClient.fetchRecentAccepted(
                    new LeetCodeClient.GraphQlRequest(
                            RECENT_ACCEPTED_QUERY,
                            Map.of("username", username, "limit", limit)
                    )
            );
            if (response.errors() != null && !response.errors().isEmpty()) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, "LeetCode could not return activity for that username.");
            }
            if (response.data() == null || response.data().recentAcSubmissionList() == null) return List.of();
            return response.data().recentAcSubmissionList().stream()
                    .map(submission -> new RecentAcceptedResponse(
                            submission.title(),
                            submission.titleSlug(),
                            Instant.ofEpochSecond(Long.parseLong(submission.timestamp()))
                    ))
                    .toList();
        } catch (FeignException exception) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "LeetCode is temporarily unavailable. Try again shortly.");
        }
    }

    public record RecentAcceptedResponse(String title, String slug, Instant acceptedAt) {
    }

    public record SolvedStats(int total, int easy, int medium, int hard) {
    }
}
