package com.preppilot.api.auth;

import com.preppilot.api.config.AppProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AppProperties.Auth properties;

    public AuthController(AuthService authService, AppProperties appProperties) {
        this.authService = authService;
        this.properties = appProperties.auth();
    }

    @PostMapping("/otp/request")
    OtpResponse requestOtp(@Valid @RequestBody RequestOtpRequest request, HttpServletRequest httpRequest) {
        AuthService.OtpRequestResult result = authService.requestOtp(request.email(), httpRequest.getRemoteAddr());
        return new OtpResponse(result.message(), result.expiresInSeconds(), result.devOtp());
    }

    @PostMapping("/otp/verify")
    ResponseEntity<UserResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        AuthService.LoginResult result = authService.verifyOtp(request.email(), request.code());
        ResponseCookie cookie = sessionCookie(result.rawToken(), properties.sessionTtl());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(UserResponse.from(result.user()));
    }

    @GetMapping("/me")
    UserResponse me(@AuthenticationPrincipal PrepPilotPrincipal principal) {
        return UserResponse.from(principal);
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        extractSessionCookie(request).ifPresent(authService::revokeSession);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, sessionCookie("", Duration.ZERO).toString())
                .build();
    }

    private ResponseCookie sessionCookie(String value, Duration maxAge) {
        return ResponseCookie.from(properties.cookie().name(), value)
                .httpOnly(true)
                .secure(properties.cookie().secure())
                .sameSite(properties.cookie().sameSite())
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    private java.util.Optional<String> extractSessionCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return java.util.Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> properties.cookie().name().equals(cookie.getName()))
                .map(jakarta.servlet.http.Cookie::getValue)
                .findFirst();
    }

    public record RequestOtpRequest(
            @NotBlank(message = "Email is required.")
            @Email(message = "Enter a valid email address.")
            String email
    ) {
    }

    public record VerifyOtpRequest(
            @NotBlank(message = "Email is required.")
            @Email(message = "Enter a valid email address.")
            String email,
            @Pattern(regexp = "\\d{6}", message = "Enter the 6-digit code.")
            String code
    ) {
    }

    public record OtpResponse(String message, long expiresInSeconds, String devOtp) {
    }

    public record UserResponse(UUID id, String email) {
        static UserResponse from(PrepPilotPrincipal principal) {
            return new UserResponse(principal.id(), principal.email());
        }
    }
}
