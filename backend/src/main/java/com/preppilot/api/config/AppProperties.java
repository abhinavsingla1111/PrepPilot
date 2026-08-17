package com.preppilot.api.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Validated
public record AppProperties(@Valid Auth auth, @Valid Web web) {

    public record Auth(
            Duration otpTtl,
            Duration sessionTtl,
            int maxOtpAttempts,
            boolean exposeDevOtp,
            String tokenPepper,
            @NotNull @Valid OtpRateLimit rateLimit,
            Cookie cookie
    ) {
        public record OtpRateLimit(
                @NotNull Duration resendCooldown,
                @NotNull Duration emailWindow,
                @Min(1) long maxRequestsPerEmail,
                @NotNull Duration ipWindow,
                @Min(1) long maxRequestsPerIp
        ) {
            public OtpRateLimit {
                if (resendCooldown != null && !resendCooldown.isPositive()
                        || emailWindow != null && !emailWindow.isPositive()
                        || ipWindow != null && !ipWindow.isPositive()) {
                    throw new IllegalArgumentException("OTP rate-limit durations must be positive");
                }
            }
        }

        public record Cookie(String name, boolean secure, String sameSite) {
        }
    }

    public record Web(List<String> allowedOrigins) {
    }
}
