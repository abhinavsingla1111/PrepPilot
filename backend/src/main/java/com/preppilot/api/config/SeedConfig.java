package com.preppilot.api.config;

import com.preppilot.api.assessment.AssessmentQuestionSeedService;
import com.preppilot.api.interview.CodingInterviewPromptSeedService;
import com.preppilot.api.problem.ProblemSeedService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedConfig {

    @Bean
    ApplicationRunner seedCuratedProblems(
            ProblemSeedService problemSeedService,
            AssessmentQuestionSeedService assessmentQuestionSeedService,
            CodingInterviewPromptSeedService codingInterviewPromptSeedService
    ) {
        return arguments -> {
            problemSeedService.seedIfEmpty();
            problemSeedService.refreshLearningOrder();
            assessmentQuestionSeedService.seedIfRequired();
            codingInterviewPromptSeedService.seedIfEmpty();
        };
    }
}
