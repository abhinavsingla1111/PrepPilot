package com.preppilot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preppilot.api.assessment.AssessmentQuestionRepository;
import com.preppilot.api.assessment.AssessmentTopic;
import com.preppilot.api.auth.OtpChallengeRepository;
import com.preppilot.api.feedback.FeedbackRequestRepository;
import com.preppilot.api.interview.CodingInterviewPromptRepository;
import com.preppilot.api.problem.Problem;
import com.preppilot.api.problem.ProblemRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PrepPilotApplicationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    AssessmentQuestionRepository assessmentQuestionRepository;

    @Autowired
    FeedbackRequestRepository feedbackRequestRepository;

    @Autowired
    CodingInterviewPromptRepository codingInterviewPromptRepository;

    @Autowired
    OtpChallengeRepository otpChallengeRepository;

    @Test
    void seedsAndFiltersTheCuratedQuestionLibrary() throws Exception {
        assertThat(problemRepository.count()).isEqualTo(145);

        mockMvc.perform(get("/api/v1/problems")
                        .param("query", "two sum")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Two Sum"))
                .andExpect(jsonPath("$.content[0].url").value("https://leetcode.com/problems/two-sum/"));
    }

    @Test
    void signsInWithOtpAndPersistsSolvedProgress() throws Exception {
        String email = "pilot@example.com";
        MvcResult otpResult = mockMvc.perform(post("/api/v1/auth/otp/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.devOtp").isString())
                .andReturn();

        JsonNode otpBody = objectMapper.readTree(otpResult.getResponse().getContentAsString());
        String code = otpBody.get("devOtp").asText();

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"code\":\"" + code + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(cookie().httpOnly("preppilot_session", true))
                .andReturn();

        Cookie sessionCookie = loginResult.getResponse().getCookie("preppilot_session");
        assertThat(sessionCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));

        Problem problem = problemRepository.findAll().getFirst();
        mockMvc.perform(put("/api/v1/progress/{problemId}", problem.getId())
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.problemId").value(problem.getId()))
                .andExpect(jsonPath("$.status").value("SOLVED"))
                .andExpect(jsonPath("$.solvedAt").isString());

        mockMvc.perform(get("/api/v1/progress").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].problemId").value(problem.getId()))
                .andExpect(jsonPath("$[0].status").value("SOLVED"));
    }

    @Test
    void throttlesRepeatedOtpRequestsWithoutStoringTheRawClientIp() throws Exception {
        String request = "{\"email\":\"rate-limit@example.com\"}";

        mockMvc.perform(post("/api/v1/auth/otp/request")
                        .with(servletRequest -> {
                            servletRequest.setRemoteAddr("198.51.100.25");
                            return servletRequest;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        assertThat(otpChallengeRepository.findFirstByEmailOrderByCreatedAtDesc("rate-limit@example.com"))
                .get()
                .extracting(challenge -> challenge.getRequestIpHash())
                .asString()
                .hasSize(64)
                .isNotEqualTo("198.51.100.25");

        mockMvc.perform(post("/api/v1/auth/otp/request")
                        .with(servletRequest -> {
                            servletRequest.setRemoteAddr("198.51.100.25");
                            return servletRequest;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value(
                        "Too many sign-in code requests. Please wait a little before trying again."
                ));
    }

    @Test
    void runsFiveBalancedNonRepeatingPracticeTestsAndKeepsAnswersPrivateUntilSubmission() throws Exception {
        assertThat(assessmentQuestionRepository.count()).isEqualTo(900);
        assertThat(assessmentQuestionRepository.countByTopic(AssessmentTopic.OPERATING_SYSTEMS)).isEqualTo(100);
        assertThat(assessmentQuestionRepository.findAll())
                .allSatisfy(question -> {
                    assertThat(question.getOptions()).hasSize(4).doesNotHaveDuplicates();
                    assertThat(question.getCorrectOption()).isBetween(0, 3);
                    assertThat(question.getJustification()).isNotBlank();
                });

        mockMvc.perform(get("/api/v1/assessments/topics"))
                .andExpect(status().isUnauthorized());

        Cookie sessionCookie = login("assessment-pilot@example.com");
        mockMvc.perform(get("/api/v1/assessments/topics").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$[0].totalQuestions").value(100))
                .andExpect(jsonPath("$[0].maxAttempts").value(5))
                .andExpect(jsonPath("$[0].durationMinutes").value(10))
                .andExpect(jsonPath("$[1].durationMinutes").value(15))
                .andExpect(jsonPath("$[8].slug").value("operating-systems"))
                .andExpect(jsonPath("$[8].durationMinutes").value(10));

        Set<String> prompts = new HashSet<>();
        for (int attemptNumber = 1; attemptNumber <= 5; attemptNumber++) {
            MvcResult startResult = mockMvc.perform(post("/api/v1/assessments/topics/oops/attempts")
                            .cookie(sessionCookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.attemptNumber").value(attemptNumber))
                    .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                    .andExpect(jsonPath("$.questions.length()").value(20))
                    .andExpect(jsonPath("$.questions[0].correctOption").doesNotExist())
                    .andExpect(jsonPath("$.questions[0].justification").doesNotExist())
                    .andReturn();

            JsonNode attempt = objectMapper.readTree(startResult.getResponse().getContentAsString());
            assertThat(Duration.between(
                    Instant.parse(attempt.get("startedAt").asText()),
                    Instant.parse(attempt.get("expiresAt").asText())
            )).isEqualTo(Duration.ofMinutes(10));
            long easy = 0;
            long medium = 0;
            long hard = 0;
            for (JsonNode question : attempt.get("questions")) {
                assertThat(prompts.add(question.get("prompt").asText())).isTrue();
                switch (question.get("difficulty").asText()) {
                    case "EASY" -> easy++;
                    case "MEDIUM" -> medium++;
                    case "HARD" -> hard++;
                    default -> throw new AssertionError("Unexpected difficulty");
                }
            }
            assertThat(easy).isEqualTo(7);
            assertThat(medium).isEqualTo(8);
            assertThat(hard).isEqualTo(5);

            String attemptId = attempt.get("id").asText();
            String firstQuestionId = attempt.get("questions").get(0).get("id").asText();
            mockMvc.perform(put("/api/v1/assessments/attempts/{attemptId}/questions/{questionId}",
                            attemptId, firstQuestionId)
                            .cookie(sessionCookie)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"selectedOption\":0}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.answeredQuestions").value(1));

            mockMvc.perform(post("/api/v1/assessments/attempts/{attemptId}/submit", attemptId)
                            .cookie(sessionCookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("COMPLETED"))
                    .andExpect(jsonPath("$.score").isNumber())
                    .andExpect(jsonPath("$.questions[0].correctOption").isNumber())
                    .andExpect(jsonPath("$.questions[0].justification").isString());
        }

        assertThat(prompts).hasSize(100);
        mockMvc.perform(post("/api/v1/assessments/topics/oops/attempts").cookie(sessionCookie))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/v1/assessments/topics/oops/history").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void storesAuthenticatedFeedbackAndValidatesTheImageContent() throws Exception {
        MockMultipartFile screenshot = new MockMultipartFile(
                "image", "navigation.png", "image/png",
                new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x01}
        );

        mockMvc.perform(multipart("/api/v1/feedback")
                        .file(screenshot)
                        .param("type", "ISSUE")
                        .param("severity", "HIGH")
                        .param("subject", "Fundamentals navigation is difficult to use")
                        .param("description", "The nested navigation should remain independently scrollable."))
                .andExpect(status().isUnauthorized());

        Cookie sessionCookie = login("feedback-pilot@example.com");
        mockMvc.perform(multipart("/api/v1/feedback")
                        .file(screenshot)
                        .cookie(sessionCookie)
                        .param("type", "ISSUE")
                        .param("severity", "HIGH")
                        .param("subject", "Fundamentals navigation is difficult to use")
                        .param("description", "The nested navigation should remain independently scrollable."))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.emailStatus").value("SKIPPED"))
                .andExpect(jsonPath("$.id").isString());

        assertThat(feedbackRequestRepository.findAll()).hasSize(1).first().satisfies(request -> {
            assertThat(request.getAttachmentContentType()).isEqualTo("image/png");
            assertThat(request.getAttachmentName()).isEqualTo("navigation.png");
            assertThat(request.getAttachmentData()).isEqualTo(screenshot.getBytes());
        });

        MockMultipartFile disguisedText = new MockMultipartFile(
                "image", "not-really.png", "image/png", "plain text".getBytes()
        );
        mockMvc.perform(multipart("/api/v1/feedback")
                        .file(disguisedText)
                        .cookie(sessionCookie)
                        .param("type", "IMPROVEMENT")
                        .param("severity", "LOW")
                        .param("subject", "A second idea")
                        .param("description", "This attachment should not be accepted."))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void schedulesMistakesAndAdvancesReviewIntervals() throws Exception {
        Cookie sessionCookie = login("revision-pilot@example.com");

        MvcResult startResult = mockMvc.perform(post("/api/v1/assessments/topics/java/attempts")
                        .cookie(sessionCookie))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode attempt = objectMapper.readTree(startResult.getResponse().getContentAsString());
        String attemptId = attempt.get("id").asText();

        mockMvc.perform(post("/api/v1/assessments/attempts/{attemptId}/submit", attemptId)
                        .cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(0));

        MvcResult reviewsResult = mockMvc.perform(get("/api/v1/reviews").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(20))
                .andExpect(jsonPath("$[0].due").value(true))
                .andExpect(jsonPath("$[0].correctOption").isNumber())
                .andExpect(jsonPath("$[0].justification").isString())
                .andReturn();
        String reviewId = objectMapper.readTree(reviewsResult.getResponse().getContentAsString()).get(0).get("id").asText();

        mockMvc.perform(post("/api/v1/reviews/{reviewId}/rating", reviewId)
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":\"REMEMBERED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intervalDays").value(3))
                .andExpect(jsonPath("$.repetitionCount").value(1))
                .andExpect(jsonPath("$.due").value(false));

        mockMvc.perform(get("/api/v1/reviews/summary").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.due").value(19))
                .andExpect(jsonPath("$.total").value(20));
    }

    @Test
    void runsOwnerScopedCodingInterviewAndRevealsGuidanceOnlyAfterSubmission() throws Exception {
        assertThat(codingInterviewPromptRepository.count()).isEqualTo(20);
        mockMvc.perform(post("/api/v1/interviews/coding/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"language\":\"JAVA\"}"))
                .andExpect(status().isUnauthorized());

        Cookie ownerCookie = login("interview-owner@example.com");
        MvcResult startResult = mockMvc.perform(post("/api/v1/interviews/coding/sessions")
                        .cookie(ownerCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"language\":\"JAVA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.solutionCode").isNotEmpty())
                .andExpect(jsonPath("$.prompt.starterCode.JAVA").isNotEmpty())
                .andExpect(jsonPath("$.prompt.starterCode.PYTHON").isNotEmpty())
                .andExpect(jsonPath("$.prompt.starterCode.CPP").isNotEmpty())
                .andExpect(jsonPath("$.prompt.sampleTests.length()").value(2))
                .andExpect(jsonPath("$.prompt.totalTests").value(6))
                .andExpect(jsonPath("$.prompt.expectedApproach").doesNotExist())
                .andExpect(jsonPath("$.prompt.expectedTimeComplexity").doesNotExist())
                .andReturn();
        String sessionId = objectMapper.readTree(startResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(put("/api/v1/interviews/coding/sessions/{sessionId}/draft", sessionId)
                        .cookie(ownerCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "language", "JAVA",
                                "solutionCode", "class Solution {}",
                                "approachNotes", "Clarify, then choose the data structure.",
                                "complexityAnalysis", "O(n)"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solutionCode").value("class Solution {}"));

        Cookie otherCookie = login("interview-other@example.com");
        mockMvc.perform(get("/api/v1/interviews/coding/sessions/{sessionId}", sessionId)
                        .cookie(otherCookie))
                .andExpect(status().isNotFound());

        Map<String, Object> submission = Map.ofEntries(
                Map.entry("language", "JAVA"),
                Map.entry("solutionCode", "class Solution {}"),
                Map.entry("approachNotes", "Clarify, then choose the data structure."),
                Map.entry("complexityAnalysis", "O(n)"),
                Map.entry("clarification", 4),
                Map.entry("approach", 4),
                Map.entry("correctness", 4),
                Map.entry("reflection", "State the invariant earlier.")
        );
        mockMvc.perform(post("/api/v1/interviews/coding/sessions/{sessionId}/submit", sessionId)
                        .cookie(ownerCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.rubric.total").value(12))
                .andExpect(jsonPath("$.prompt.expectedApproach").isString())
                .andExpect(jsonPath("$.prompt.expectedTimeComplexity").isString());

        mockMvc.perform(get("/api/v1/interviews/coding/sessions").cookie(ownerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bestPassedTests").value(nullValue()))
                .andExpect(jsonPath("$[0].totalTests").value(nullValue()));

        mockMvc.perform(get("/api/v1/interviews/coding/runner").cookie(ownerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.dailyLimit").value(5));
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
        Cookie sessionCookie = loginResult.getResponse().getCookie("preppilot_session");
        assertThat(sessionCookie).isNotNull();
        return sessionCookie;
    }
}
