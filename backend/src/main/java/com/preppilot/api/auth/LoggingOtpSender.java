package com.preppilot.api.auth;

import com.preppilot.api.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingOtpSender implements OtpSender {

    private static final Logger log = LoggerFactory.getLogger(LoggingOtpSender.class);
    private final boolean exposeDevOtp;

    public LoggingOtpSender(AppProperties properties) {
        this.exposeDevOtp = properties.auth().exposeDevOtp();
    }

    @Override
    public void send(UUID challengeId, String email, String code, Instant expiresAt) {
        if (exposeDevOtp) {
            log.info("Development OTP for {} is {} (expires at {})", email, code, expiresAt);
            return;
        }
        log.info("OTP delivery skipped because email and development preview are disabled");
    }
}
