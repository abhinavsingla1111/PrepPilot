package com.preppilot.api.integration.leetcode;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
public class LeetCodeFeignConfig {

    @Bean
    Logger.Level leetCodeFeignLogLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    Request.Options leetCodeRequestOptions() {
        return new Request.Options(3, TimeUnit.SECONDS, 6, TimeUnit.SECONDS, true);
    }

    @Bean
    Retryer leetCodeRetryer() {
        return new Retryer.Default(250, 1_000, 2);
    }
}
