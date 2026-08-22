
package com.preppilot.api.feedback;

import com.preppilot.api.common.ApiException;
import com.preppilot.api.integration.email.BrevoEmailClient;
import com.preppilot.api.integration.email.BrevoEmailGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
@ConditionalOnExpression("${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'brevo'")
public class BrevoFeedbackNotifier implements FeedbackNotifier {

    private static final Logger log = LoggerFactory.getLogger(BrevoFeedbackNotifier.class);

    private final BrevoEmailGateway emailGateway;
    private final String recipient;

    public BrevoFeedbackNotifier(
            BrevoEmailGateway emailGateway,
            @Value("${app.feedback.recipient-email:}") String recipient
    ) {
        this.emailGateway = emailGateway;
        this.recipient = recipient;
    }

    @Override
    public FeedbackEmailStatus send(FeedbackRequest request) {
        if (recipient == null || recipient.isBlank()) {
            log.warn("Feedback email not sent because no recipient is configured; requestId={}", request.getId());
            return FeedbackEmailStatus.FAILED;
        }

        List<BrevoEmailClient.Attachment> attachments = request.getAttachmentData() == null
                ? null
                : List.of(new BrevoEmailClient.Attachment(
                        Base64.getEncoder().encodeToString(request.getAttachmentData()),
                        request.getAttachmentName()
                ));
        String subject = "[PrepPilot %s] %s · %s".formatted(
                request.getSeverity().name(),
                request.getType() == FeedbackType.ISSUE ? "Issue" : "Improvement",
                SmtpFeedbackNotifier.safeSubject(request.getSubject())
        );
        String text = """
                %s

                %s

                From: %s
                Severity: %s
                Request ID: %s
                """.formatted(
                request.getSubject(),
                request.getDescription(),
                request.getUser().getEmail(),
                request.getSeverity(),
                request.getId()
        );

        try {
            emailGateway.send(
                    recipient,
                    subject,
                    SmtpFeedbackNotifier.htmlBody(request),
                    text,
                    request.getUser().getEmail(),
                    attachments
            );
            return FeedbackEmailStatus.SENT;
        } catch (ApiException exception) {
            log.warn("Could not send feedback email; requestId={}", request.getId());
            return FeedbackEmailStatus.FAILED;
        }
    }
}
