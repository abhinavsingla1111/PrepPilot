package com.preppilot.api.integration.leetcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "leetcode",
        url = "${clients.leetcode.base-url}",
        configuration = LeetCodeFeignConfig.class
)
public interface LeetCodeClient {

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE)
    GraphQlResponse fetchRecentAccepted(@RequestBody GraphQlRequest request);

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE)
    GraphQlResponse fetchUserStats(@RequestBody GraphQlRequest request);

    record GraphQlRequest(String query, Map<String, Object> variables) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GraphQlResponse(GraphQlData data, List<GraphQlError> errors) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GraphQlData(List<RecentSubmission> recentAcSubmissionList, MatchedUser matchedUser) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record RecentSubmission(String title, String titleSlug, String timestamp) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record MatchedUser(SubmitStats submitStatsGlobal) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SubmitStats(List<AcSubmissionNum> acSubmissionNum) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record AcSubmissionNum(String difficulty, int count) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GraphQlError(String message) {
    }
}
