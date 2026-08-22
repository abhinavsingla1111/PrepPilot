

package com.preppilot.api.integration.email;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "brevoEmail",
        url = "https://api.brevo.com",
        configuration = BrevoFeignConfiguration.class
)
public interface BrevoEmailClient {

    @PostMapping(value = "/v3/smtp/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    SendEmailResponse send(@RequestBody SendEmailRequest request);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record SendEmailRequest(
            Sender sender,
            List<Recipient> to,
            String subject,
            String htmlContent,
            String textContent,
            Recipient replyTo,
            List<Attachment> attachment
    ) {
    }

    record Sender(String name, String email) {
    }

    record Recipient(String email) {
    }

    record Attachment(String content, String name) {
    }

    record SendEmailResponse(String messageId) {
    }
}
