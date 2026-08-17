package com.preppilot.api.feedback;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnExpression("${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'smtp'")
public class SmtpFeedbackNotifier implements FeedbackNotifier {

    private static final Logger log = LoggerFactory.getLogger(SmtpFeedbackNotifier.class);

    private final JavaMailSender mailSender;
    private final String from;
    private final String recipient;

    public SmtpFeedbackNotifier(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String from,
            @Value("${app.feedback.recipient-email:}") String configuredRecipient,
            @Value("${spring.mail.username:}") String mailUsername
    ) {
        this.mailSender = mailSender;
        this.from = from;
        this.recipient = configuredRecipient == null || configuredRecipient.isBlank()
                ? mailUsername
                : configuredRecipient;
    }

    @Override
    public FeedbackEmailStatus send(FeedbackRequest request) {
        if (recipient == null || recipient.isBlank()) {
            log.warn("Feedback email not sent because no recipient is configured; requestId={}", request.getId());
            return FeedbackEmailStatus.FAILED;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    request.getAttachmentData() != null,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(recipient);
            helper.setReplyTo(request.getUser().getEmail());
            helper.setSubject("[PrepPilot %s] %s · %s".formatted(
                    request.getSeverity().name(),
                    request.getType() == FeedbackType.ISSUE ? "Issue" : "Improvement",
                    safeSubject(request.getSubject())
            ));
            helper.setText(htmlBody(request), true);

            byte[] attachment = request.getAttachmentData();
            if (attachment != null) {
                helper.addAttachment(
                        request.getAttachmentName(),
                        () -> new java.io.ByteArrayInputStream(attachment),
                        request.getAttachmentContentType()
                );
            }

            mailSender.send(message);
            log.info("Sent feedback notification; requestId={}", request.getId());
            return FeedbackEmailStatus.SENT;
        } catch (MailException | MessagingException exception) {
            log.error("Failed to send feedback notification; requestId={}", request.getId(), exception);
            return FeedbackEmailStatus.FAILED;
        }
    }

    static String htmlBody(FeedbackRequest request) {
        String typeLabel = request.getType() == FeedbackType.ISSUE ? "Issue report" : "Improvement idea";
        String attachment = request.getAttachmentName() == null
                ? "No image attached"
                : HtmlUtils.htmlEscape(request.getAttachmentName()) + " · " + request.getAttachmentSize() + " bytes";
        return """
                <!doctype html>
                <html><body style="margin:0;background:#080b0a;color:#eef2ea;font-family:Arial,sans-serif;padding:32px">
                  <div style="max-width:680px;margin:auto;background:#101411;border:1px solid #2a332b;border-radius:20px;overflow:hidden">
                    <div style="padding:28px 32px;background:linear-gradient(135deg,#182012,#101411)">
                      <div style="color:#c9ff63;font-size:12px;font-weight:700;letter-spacing:2px;text-transform:uppercase">PrepPilot feedback</div>
                      <h1 style="margin:12px 0 0;font-size:28px;line-height:1.2;color:#f3f6f1">%s</h1>
                    </div>
                    <div style="padding:28px 32px">
                      <div style="display:inline-block;padding:7px 11px;border-radius:999px;background:#c9ff63;color:#10150d;font-size:12px;font-weight:700">%s · %s</div>
                      <p style="margin:24px 0 8px;color:#8d978f;font-size:12px;text-transform:uppercase;letter-spacing:1px">Description</p>
                      <p style="margin:0;color:#e7ece7;font-size:16px;line-height:1.65;white-space:pre-wrap">%s</p>
                      <div style="margin-top:28px;padding:18px;border-radius:12px;background:#0b0f0c;border:1px solid #252d26;color:#aeb7af;font-size:14px;line-height:1.6">
                        From: %s<br>Attachment: %s<br>Request ID: %s
                      </div>
                    </div>
                  </div>
                </body></html>
                """.formatted(
                HtmlUtils.htmlEscape(request.getSubject()),
                typeLabel,
                request.getSeverity().name(),
                HtmlUtils.htmlEscape(request.getDescription()),
                HtmlUtils.htmlEscape(request.getUser().getEmail()),
                attachment,
                request.getId()
        );
    }

    static String safeSubject(String value) {
        return value.replace('\r', ' ').replace('\n', ' ').trim();
    }
}
