package com.preppilot.api;

import com.preppilot.api.config.AppProperties;
import com.preppilot.api.interview.execution.RunnerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients
@EnableConfigurationProperties({AppProperties.class, RunnerProperties.class})
public class PrepPilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrepPilotApplication.class, args);
    }
}
