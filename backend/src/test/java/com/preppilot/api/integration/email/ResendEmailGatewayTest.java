package com.preppilot.api.integration.email;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResendEmailGatewayTest {

    @Test
    void sendsStructuredEmailWithAnIdempotencyKey() {
        ResendEmailClient client = mock(ResendEmailClient.class);
        when(client.send(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new ResendEmailClient.SendEmailResponse("email-id"));
        ResendEmailGateway gateway = new ResendEmailGateway(
                client,
                "PrepPilot <sign-in@example.com>",
                "test-api-key"
        );

        gateway.send(
                "otp-challenge-id",
                "pilot@example.com",
                "Sign in",
                "<p>123456</p>",
                "123456",
                null,
                null
        );

        ArgumentCaptor<ResendEmailClient.SendEmailRequest> requestCaptor =
                ArgumentCaptor.forClass(ResendEmailClient.SendEmailRequest.class);
        verify(client).send(org.mockito.ArgumentMatchers.eq("otp-challenge-id"), requestCaptor.capture());
        assertThat(requestCaptor.getValue().from()).isEqualTo("PrepPilot <sign-in@example.com>");
        assertThat(requestCaptor.getValue().to()).containsExactly("pilot@example.com");
        assertThat(requestCaptor.getValue().attachments()).isNull();
    }
}
