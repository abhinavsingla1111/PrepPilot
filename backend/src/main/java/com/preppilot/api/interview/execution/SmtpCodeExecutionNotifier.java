package com.preppilot.api.interview.execution;

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
@ConditionalOnExpression("${app.runner.email-results:false} and ${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'smtp'")
public class SmtpCodeExecutionNotifier implements CodeExecutionNotifier {

    private static final Logger log = LoggerFactory.getLogger(SmtpCodeExecutionNotifier.class);

    private final JavaMailSender mailSender;
    private final String from;
    private final String publicUrl;

    public SmtpCodeExecutionNotifier(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String from,
            @Value("${app.public-url:http://localhost:5173}") String publicUrl
    ) {
        this.mailSender = mailSender;
        this.from = from;
        this.publicUrl = publicUrl;
    }

    @Override
    public void notify(CodingExecutionStore.Completion completion) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(completion.email());
            helper.setSubject("PrepPilot result: %d/%d tests passed".formatted(completion.passed(), completion.total()));
            helper.setText(body(completion), true);
            mailSender.send(message);
            log.info("Sent coding execution result; submissionId={}", completion.id());
        } catch (MailException | MessagingException exception) {
            log.warn("Could not email coding execution result; submissionId={}", completion.id());
        }
    }

    private String body(CodingExecutionStore.Completion completion) {
        String title = HtmlUtils.htmlEscape(completion.problemTitle());
        String url = HtmlUtils.htmlEscape(publicUrl);
        return """
                <!doctype html><html><body style="margin:0;padding:32px;background:#07090d;color:#f5f7fb;font-family:Arial,sans-serif">
                  <div style="max-width:620px;margin:auto;padding:30px;border:1px solid #263140;border-radius:20px;background:#0e141d">
                    <div style="color:#0a84ff;font-size:12px;font-weight:800;letter-spacing:2px;text-transform:uppercase">PrepPilot Interview Lab</div>
                    <h1 style="margin:12px 0 8px;font-size:28px">Your code has finished running.</h1>
                    <p style="color:#a9b4c2;font-size:16px;line-height:1.6">%s</p>
                    <div style="margin:24px 0;padding:20px;border-radius:14px;background:#101d2c;font-size:24px;font-weight:800;color:#78d7a5">%d / %d tests passed</div>
                    <a href="%s" style="display:inline-block;padding:12px 18px;border-radius:999px;background:#0a84ff;color:white;text-decoration:none;font-weight:700">Open Interview Lab</a>
                    <p style="margin-top:24px;color:#788493;font-size:12px">Your source code and hidden test data are intentionally not included in email.</p>
                  </div>
                </body></html>
                """.formatted(title, completion.passed(), completion.total(), url);
    }
}
