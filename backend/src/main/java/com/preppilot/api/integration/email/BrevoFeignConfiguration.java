

package com.preppilot.api.integration.email;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class BrevoFeignConfiguration {

    @Bean
    RequestInterceptor brevoAuthentication(@Value("${app.mail.brevo.api-key:}") String apiKey) {
        return template -> {
            template.header("api-key", apiKey);
            template.header("User-Agent", "PrepPilot/1.0");
        };
    }

    @Bean
    Retryer brevoRetryer() {
        // Brevo has no idempotency key, so a retry could duplicate delivery. Never retry.
        return Retryer.NEVER_RETRY;
    }
}
