package com.preppilot.api.integration.email;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ResendFeignConfiguration {

    @Bean
    RequestInterceptor resendAuthentication(@Value("${app.mail.resend.api-key:}") String apiKey) {
        return template -> {
            template.header("Authorization", "Bearer " + apiKey);
            template.header("User-Agent", "PrepPilot/1.0");
        };
    }

    @Bean
    Retryer resendRetryer() {
        // The request carries a Resend idempotency key, so one network retry cannot duplicate delivery.
        return new Retryer.Default(250, 1_000, 2);
    }
}
