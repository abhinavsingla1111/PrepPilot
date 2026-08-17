package com.preppilot.api.auth;

import com.preppilot.api.common.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@ConditionalOnExpression("${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'smtp'")
public class SmtpOtpSender implements OtpSender {

    private static final Logger log = LoggerFactory.getLogger(SmtpOtpSender.class);

    private final JavaMailSender mailSender;
    private final String from;

    public SmtpOtpSender(JavaMailSender mailSender, @Value("${app.mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(UUID challengeId, String email, String code, Instant expiresAt) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Your PrepPilot verification code");
        message.setText("""
                Your PrepPilot verification code is: %s

                Enter it in the app to sign in. The code expires shortly.
                If you didn't request this, you can safely ignore this email.
                """.formatted(code));

        try {
            mailSender.send(message);
            log.info("Sent OTP email to {}", email);
        } catch (MailException exception) {
            log.error("Failed to send OTP email to {}", email, exception);
            throw new ApiException(HttpStatus.BAD_GATEWAY, "We couldn't send the code right now. Please try again shortly.");
        }
    }
}
