package com.preppilot.api.auth;

import java.util.UUID;

public record PrepPilotPrincipal(UUID id, String email) {
}
