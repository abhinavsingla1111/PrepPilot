package com.preppilot.api.interview.execution;

import com.preppilot.api.common.ApiException;
import com.preppilot.api.integration.email.ResendEmailGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
@ConditionalOnExpression("${app.runner.email-results:false} and ${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'resend'")
public class ResendCodeExecutionNotifier implements CodeExecutionNotifier {

    private static final Logger log = LoggerFactory.getLogger(ResendCodeExecutionNotifier.class);

    private final ResendEmailGateway emailGateway;
    private final String publicUrl;

    public ResendCodeExecutionNotifier(
            ResendEmailGateway emailGateway,
            @Value("${app.public-url:http://localhost:5173}") String publicUrl
    ) {
        this.emailGateway = emailGateway;
        this.publicUrl = publicUrl;
    }

    @Override
    public void notify(CodingExecutionStore.Completion completion) {
        String subject = "PrepPilot result: %d/%d tests passed".formatted(completion.passed(), completion.total());
        String text = "%s: %d/%d tests passed. Open %s to review the result.".formatted(
                completion.problemTitle(),
                completion.passed(),
                completion.total(),
                publicUrl
        );
        try {
            emailGateway.send(
                    "execution-" + completion.id(),
                    completion.email(),
                    subject,
                    body(completion),
                    text,
                    null,
                    null
            );
        } catch (ApiException exception) {
            log.warn("Could not email coding execution result; submissionId={}", completion.id());
        }
    }

    private String body(CodingExecutionStore.Completion completion) {
        String title = HtmlUtils.htmlEscape(completion.problemTitle());
        String url = HtmlUtils.htmlEscape(publicUrl);
        return """
                <!doctype html><html><body style="margin:0;padding:32px;background:#07100b;color:#f5f8f3;font-family:Arial,sans-serif">
                  <div style="max-width:620px;margin:auto;padding:30px;border:1px solid #26362c;border-radius:20px;background:#0d1711">
                    <div style="color:#a9e85c;font-size:12px;font-weight:800;letter-spacing:2px;text-transform:uppercase">PrepPilot Interview Lab</div>
                    <h1 style="margin:12px 0 8px;font-size:28px">Your code has finished running.</h1>
                    <p style="color:#a9b6ac;font-size:16px;line-height:1.6">%s</p>
                    <div style="margin:24px 0;padding:20px;border-radius:14px;background:#112016;font-size:24px;font-weight:800;color:#a9e85c">%d / %d tests passed</div>
                    <a href="%s" style="display:inline-block;padding:12px 18px;border-radius:999px;background:#73b92d;color:#07100b;text-decoration:none;font-weight:700">Open Interview Lab</a>
                    <p style="margin-top:24px;color:#7f8d82;font-size:12px">Your source code and hidden test data are intentionally not included in email.</p>
                  </div>
                </body></html>
                """.formatted(title, completion.passed(), completion.total(), url);
    }
}
