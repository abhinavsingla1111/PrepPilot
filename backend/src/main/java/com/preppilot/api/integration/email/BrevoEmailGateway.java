

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
@ConditionalOnExpression("${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'brevo'")
public class BrevoEmailGateway {

    private static final Logger log = LoggerFactory.getLogger(BrevoEmailGateway.class);

    private final BrevoEmailClient client;
    private final BrevoEmailClient.Sender sender;

    public BrevoEmailGateway(
            BrevoEmailClient client,
            @Value("${app.mail.from}") String from,
            @Value("${app.mail.brevo.api-key:}") String apiKey
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("BREVO_API_KEY is required when MAIL_PROVIDER=brevo");
        }
        this.client = client;
        this.sender = parseSender(from);
    }

    public void send(
            String to,
            String subject,
            String html,
            String text,
            String replyTo,
            List<BrevoEmailClient.Attachment> attachments
    ) {
        try {
            BrevoEmailClient.SendEmailResponse response = client.send(
                    new BrevoEmailClient.SendEmailRequest(
                            sender,
                            List.of(new BrevoEmailClient.Recipient(to)),
                            subject,
                            html,
                            text,
                            replyTo == null || replyTo.isBlank() ? null : new BrevoEmailClient.Recipient(replyTo),
                            attachments == null || attachments.isEmpty() ? null : List.copyOf(attachments)
                    )
            );
            log.info("Email accepted by Brevo; messageId={}", response == null ? null : response.messageId());
        } catch (FeignException exception) {
            log.warn("Brevo rejected an email request; status={}", exception.status());
            throw new ApiException(
                    HttpStatus.BAD_GATEWAY,
                    "We couldn't send the email right now. Please try again shortly."
            );
        }
    }

    // MAIL_FROM may be "PrepPilot <sender@example.com>" or a bare address; Brevo needs the parts split.
    private static BrevoEmailClient.Sender parseSender(String from) {
        if (from == null || from.isBlank()) {
            throw new IllegalStateException("MAIL_FROM is required when MAIL_PROVIDER=brevo");
        }
        String value = from.trim();
        int open = value.indexOf('<');
        int close = value.indexOf('>');
        if (open >= 0 && close > open) {
            String name = value.substring(0, open).trim();
            String email = value.substring(open + 1, close).trim();
            return new BrevoEmailClient.Sender(name.isBlank() ? null : name, email);
        }
        return new BrevoEmailClient.Sender(null, value);
    }
}
