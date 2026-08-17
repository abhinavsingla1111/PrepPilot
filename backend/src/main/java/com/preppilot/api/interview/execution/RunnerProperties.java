package com.preppilot.api.interview.execution;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.runner")
public record RunnerProperties(
        boolean enabled,
        String workerToken,
        int dailyRunLimit,
        Duration minimumRunInterval,
        Duration leaseDuration,
        int maxAttempts,
        boolean emailResults,
        int maximumDiagnosticCharacters,
        int maximumOutputCharacters
) {
    public RunnerProperties {
        workerToken = safe(workerToken);
    }

    public void validate() {
        if (!enabled) return;
        if (workerToken.length() < 32) {
            throw new IllegalStateException("RUNNER_WORKER_TOKEN must contain at least 32 characters.");
        }
        if (dailyRunLimit < 1 || dailyRunLimit > 200 || maxAttempts < 1 || maxAttempts > 10) {
            throw new IllegalStateException("Code runner quota or retry configuration is outside its safe range.");
        }
        if (minimumRunInterval == null || minimumRunInterval.isNegative()
                || leaseDuration == null || leaseDuration.isNegative() || leaseDuration.isZero()
                || leaseDuration.compareTo(Duration.ofSeconds(20)) < 0
                || leaseDuration.compareTo(Duration.ofMinutes(10)) > 0) {
            throw new IllegalStateException("Code runner timing configuration must be positive.");
        }
        if (maximumDiagnosticCharacters < 500 || maximumDiagnosticCharacters > 16_000
                || maximumOutputCharacters < 1_000 || maximumOutputCharacters > 64_000) {
            throw new IllegalStateException("Code runner output limits are outside their safe range.");
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

}
