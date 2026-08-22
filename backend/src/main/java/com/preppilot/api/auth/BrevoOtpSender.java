package com.preppilot.api.auth;

import com.preppilot.api.integration.email.BrevoEmailGateway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@ConditionalOnExpression("${app.mail.enabled:false} and '${app.mail.provider:smtp}' == 'brevo'")
public class BrevoOtpSender implements OtpSender {

    private final BrevoEmailGateway emailGateway;

    public BrevoOtpSender(BrevoEmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @Override
    public void send(UUID challengeId, String email, String code, Instant expiresAt) {
        String subject = "Your PrepPilot verification code";
        String text = """
                Your PrepPilot verification code is: %s

                Enter it in the app to sign in. The code expires shortly.
                If you didn't request this, you can safely ignore this email.
                """.formatted(code);
        String html = """
                <!doctype html>
                <html><body style="margin:0;padding:32px;background:#07100b;color:#f4f8f2;font-family:Arial,sans-serif">
                  <div style="max-width:560px;margin:auto;overflow:hidden;border:1px solid #26362c;border-radius:22px;background:#0d1711">
                    <div style="padding:30px 34px;background:linear-gradient(135deg,#13251a,#0d1711)">
                      <div style="color:#a9e85c;font-size:12px;font-weight:800;letter-spacing:2px;text-transform:uppercase">PrepPilot secure sign-in</div>
                      <h1 style="margin:12px 0 0;font-size:28px;line-height:1.25">Your one-time code</h1>
                    </div>
                    <div style="padding:30px 34px">
                      <div style="padding:20px;border:1px solid #3b5b3d;border-radius:14px;background:#111f15;color:#c8ff73;font-family:monospace;font-size:34px;font-weight:800;letter-spacing:9px;text-align:center">%s</div>
                      <p style="margin:24px 0 8px;color:#dce6dc;font-size:15px;line-height:1.65">Enter this code in PrepPilot to finish signing in.</p>
                      <p style="margin:0;color:#8f9d92;font-size:13px;line-height:1.6">It expires shortly and can be used once. If you did not request it, you can safely ignore this email.</p>
                    </div>
                  </div>
                </body></html>
                """.formatted(code);
        emailGateway.send(email, subject, html, text, null, null);
    }
}