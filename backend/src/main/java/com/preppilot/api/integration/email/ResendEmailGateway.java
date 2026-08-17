package com.preppilot.api.integration.email;

import com.preppilot.api.common.ApiException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnExpression("${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'resend'")
public class ResendEmailGateway {

    private static final Logger log = LoggerFactory.getLogger(ResendEmailGateway.class);

    private final ResendEmailClient client;
    private final String from;

    public ResendEmailGateway(
            ResendEmailClient client,
            @Value("${app.mail.from}") String from,
            @Value("${app.mail.resend.api-key:}") String apiKey
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("RESEND_API_KEY is required when MAIL_PROVIDER=resend");
        }
        this.client = client;
        this.from = from;
    }

    public void send(
            String idempotencyKey,
            String to,
            String subject,
            String html,
            String text,
            String replyTo,
            List<ResendEmailClient.Attachment> attachments
    ) {
        try {
            ResendEmailClient.SendEmailResponse response = client.send(
                    idempotencyKey,
                    new ResendEmailClient.SendEmailRequest(
                            from,
                            List.of(to),
                            subject,
                            html,
                            text,
                            replyTo,
                            attachments == null || attachments.isEmpty() ? null : List.copyOf(attachments)
                    )
            );
            log.info("Email accepted by Resend; messageId={}", response.id());
        } catch (FeignException exception) {
            log.warn("Resend rejected an email request; status={}", exception.status());
            throw new ApiException(
                    HttpStatus.BAD_GATEWAY,
                    "We couldn't send the email right now. Please try again shortly."
            );
        }
    }
}
