package com.preppilot.api.integration.email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "resendEmail",
        url = "https://api.resend.com",
        configuration = ResendFeignConfiguration.class
)
public interface ResendEmailClient {

    @PostMapping(value = "/emails", consumes = MediaType.APPLICATION_JSON_VALUE)
    SendEmailResponse send(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody SendEmailRequest request
    );

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record SendEmailRequest(
            String from,
            List<String> to,
            String subject,
            String html,
            String text,
            @JsonProperty("reply_to") String replyTo,
            List<Attachment> attachments
    ) {
    }

    record Attachment(String content, String filename) {
    }

    record SendEmailResponse(String id) {
    }
}
