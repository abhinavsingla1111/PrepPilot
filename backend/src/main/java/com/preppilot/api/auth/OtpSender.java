package com.preppilot.api.auth;

import java.time.Instant;
import java.util.UUID;

public interface OtpSender {
    void send(UUID challengeId, String email, String code, Instant expiresAt);
}
