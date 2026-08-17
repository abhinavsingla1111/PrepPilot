package com.preppilot.api.interview.execution;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RunnerQueueIntegrationTest {

    private static final String WORKER_TOKEN = UUID.randomUUID() + "-" + UUID.randomUUID();

    @DynamicPropertySource
    static void runnerProperties(DynamicPropertyRegistry registry) {
        registry.add("app.runner.enabled", () -> true);
        registry.add("app.runner.worker-token", () -> WORKER_TOKEN);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void claimsAndCompletesAQueuedRunThroughAuthenticatedLeases() throws Exception {
        Cookie owner = login("runner-owner@example.com");
        MvcResult sessionResult = mockMvc.perform(post("/api/v1/interviews/coding/sessions")
                        .cookie(owner)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"language\":\"JAVA\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode session = objectMapper.readTree(sessionResult.getResponse().getContentAsString());
        String sessionId = session.get("id").asText();
        String sourceCode = session.get("solutionCode").asText();

        MvcResult submissionResult = mockMvc.perform(post("/api/v1/interviews/coding/sessions/{sessionId}/runs", sessionId)
                        .cookie(owner)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "language", "JAVA",
                                "sourceCode", sourceCode
                        ))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("QUEUED"))
                .andReturn();
        String submissionId = objectMapper.readTree(submissionResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/internal/runner/jobs/claim"))
                .andExpect(status().isUnauthorized());

        MvcResult claimResult = mockMvc.perform(post("/api/internal/runner/jobs/claim")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + WORKER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(submissionId))
                .andExpect(jsonPath("$.language").value("JAVA"))
                .andExpect(jsonPath("$.sourceCode").value(sourceCode))
                .andExpect(jsonPath("$.cases.length()").value(6))
                .andExpect(jsonPath("$.cases[0].input").isString())
                .andExpect(jsonPath("$.cases[0].expectedOutput").doesNotExist())
                .andReturn();
        JsonNode claim = objectMapper.readTree(claimResult.getResponse().getContentAsString());
        String lease = claim.get("leaseToken").asText();
        assertThat(lease).hasSizeGreaterThanOrEqualTo(32);

        List<Map<String, Object>> results = new ArrayList<>();
        for (int position = 1; position <= 6; position++) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("position", position);
            result.put("status", "COMPILE_ERROR");
            result.put("actualOutput", "");
            result.put("diagnostic", "Synthetic compiler failure for the queue contract test.");
            result.put("timeSeconds", "0");
            results.add(result);
        }
        mockMvc.perform(post("/api/internal/runner/jobs/{submissionId}/complete", submissionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + WORKER_TOKEN)
                        .header("X-Runner-Lease", lease)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("results", results))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.passed").value(0))
                .andExpect(jsonPath("$.total").value(6));

        mockMvc.perform(get("/api/v1/interviews/coding/runs/{submissionId}", submissionId).cookie(owner))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.results.length()").value(6))
                .andExpect(jsonPath("$.results[0].status").value("COMPILE_ERROR"))
                .andExpect(jsonPath("$.results[2].input").value(nullValue()))
                .andExpect(jsonPath("$.results[2].expectedOutput").value(nullValue()))
                .andExpect(jsonPath("$.results[2].actualOutput").value(nullValue()));

        mockMvc.perform(post("/api/internal/runner/jobs/{submissionId}/complete", submissionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + WORKER_TOKEN)
                        .header("X-Runner-Lease", lease)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("results", results))))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/v1/interviews/coding/sessions/{sessionId}/submit", sessionId)
                        .cookie(owner)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.ofEntries(
                                Map.entry("language", "JAVA"),
                                Map.entry("solutionCode", sourceCode),
                                Map.entry("approachNotes", "Use the required data structure."),
                                Map.entry("complexityAnalysis", "O(n)"),
                                Map.entry("clarification", 3),
                                Map.entry("approach", 3),
                                Map.entry("correctness", 1),
                                Map.entry("reflection", "Verify compilation before submission.")
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mockMvc.perform(get("/api/v1/interviews/coding/sessions").cookie(owner))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bestPassedTests").value(0))
                .andExpect(jsonPath("$[0].totalTests").value(6))
                .andExpect(jsonPath("$[0].rubricTotal").doesNotExist());

        mockMvc.perform(post("/api/internal/runner/jobs/claim")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + WORKER_TOKEN))
                .andExpect(status().isNoContent());
    }

    private Cookie login(String email) throws Exception {
        MvcResult otpResult = mockMvc.perform(post("/api/v1/auth/otp/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String code = objectMapper.readTree(otpResult.getResponse().getContentAsString()).get("devOtp").asText();
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"code\":\"" + code + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        Cookie cookie = loginResult.getResponse().getCookie("preppilot_session");
        assertThat(cookie).isNotNull();
        return cookie;
    }
}
