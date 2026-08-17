package com.preppilot.api.interview.execution;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RunnerConfig {

    @Bean
    ApplicationRunner validateRunnerConfiguration(RunnerProperties properties) {
        return arguments -> properties.validate();
    }
}
