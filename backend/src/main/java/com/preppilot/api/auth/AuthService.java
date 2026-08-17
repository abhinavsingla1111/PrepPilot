package com.preppilot.api.auth;

import com.preppilot.api.common.ApiException;
import com.preppilot.api.config.AppProperties;
import com.preppilot.api.user.AppUser;
import com.preppilot.api.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private final OtpChallengeRepository otpRepository;
    private final AuthSessionRepository sessionRepository;
    private final AppUserRepository userRepository;
    private final OtpSender otpSender;
    private final PasswordEncoder passwordEncoder;
    private final TokenHasher tokenHasher;
    private final SecureRandom secureRandom;
    private final Clock clock;
    private final AppProperties.Auth properties;

    public AuthService(
            OtpChallengeRepository otpRepository,
            AuthSessionRepository sessionRepository,
            AppUserRepository userRepository,
            OtpSender otpSender,
            PasswordEncoder passwordEncoder,
            TokenHasher tokenHasher,
            SecureRandom secureRandom,
            Clock clock,
            AppProperties appProperties
    ) {
        this.otpRepository = otpRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.otpSender = otpSender;
        this.passwordEncoder = passwordEncoder;
        this.tokenHasher = tokenHasher;
        this.secureRandom = secureRandom;
        this.clock = clock;
        this.properties = appProperties.auth();
    }

    @Transactional(noRollbackFor = ApiException.class)
    public OtpRequestResult requestOtp(String emailInput, String clientIp) {
        String email = normalizeEmail(emailInput);
        Instant now = clock.instant();
        String requestIpHash = tokenHasher.hash("otp-ip:" + normalizedClientIp(clientIp));
        enforceRequestRateLimits(email, requestIpHash, now);

        String code = "%06d".formatted(secureRandom.nextInt(1_000_000));
        Instant expiresAt = now.plus(properties.otpTtl());
        UUID challengeId = UUID.randomUUID();

        OtpChallenge challenge = new OtpChallenge(
                challengeId,
                email,
                passwordEncoder.encode(code),
                expiresAt,
                now,
                requestIpHash
        );
        otpRepository.save(challenge);
        otpSender.send(challengeId, email, code, expiresAt);

        return new OtpRequestResult(
                "If the address is valid, a sign-in code is on its way.",
                properties.otpTtl().toSeconds(),
                properties.exposeDevOtp() ? code : null
        );
    }

    @Transactional(noRollbackFor = ApiException.class)
    public LoginResult verifyOtp(String emailInput, String code) {
        String email = normalizeEmail(emailInput);
        Instant now = clock.instant();
        OtpChallenge challenge = otpRepository.findFirstByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> unauthorized("That code is invalid or has expired."));

        if (challenge.getConsumedAt() != null
                || !challenge.getExpiresAt().isAfter(now)
                || challenge.getAttemptCount() >= properties.maxOtpAttempts()) {
            throw unauthorized("That code is invalid or has expired.");
        }

        if (!passwordEncoder.matches(code, challenge.getCodeHash())) {
            challenge.recordFailedAttempt();
            otpRepository.save(challenge);
            throw unauthorized("That code is invalid or has expired.");
        }

        challenge.consume(now);
        AppUser user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new AppUser(UUID.randomUUID(), email, now)));

        String rawToken = createSessionToken();
        Instant sessionExpiry = now.plus(properties.sessionTtl());
        sessionRepository.save(new AuthSession(
                UUID.randomUUID(),
                user,
                tokenHasher.hash(rawToken),
                sessionExpiry,
                now
        ));

        return new LoginResult(new PrepPilotPrincipal(user.getId(), user.getEmail()), rawToken, sessionExpiry);
    }

    @Transactional
    public void revokeSession(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return;
        Instant now = clock.instant();
        sessionRepository.findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(tokenHasher.hash(rawToken), now)
                .ifPresent(session -> session.revoke(now));
    }

    private String createSessionToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String normalizeEmail(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }

    private String normalizedClientIp(String clientIp) {
        if (clientIp == null || clientIp.isBlank()) {
            return "unknown";
        }
        return clientIp.strip().toLowerCase(Locale.ROOT);
    }

    private void enforceRequestRateLimits(String email, String requestIpHash, Instant now) {
        AppProperties.Auth.OtpRateLimit limits = properties.rateLimit();
        otpRepository.findFirstByEmailOrderByCreatedAtDesc(email)
                .filter(challenge -> challenge.getCreatedAt().isAfter(now.minus(limits.resendCooldown())))
                .ifPresent(challenge -> {
                    throw tooManyRequests();
                });

        long emailRequests = otpRepository.countByEmailAndCreatedAtGreaterThanEqual(
                email,
                now.minus(limits.emailWindow())
        );
        long ipRequests = otpRepository.countByRequestIpHashAndCreatedAtGreaterThanEqual(
                requestIpHash,
                now.minus(limits.ipWindow())
        );
        if (emailRequests >= limits.maxRequestsPerEmail() || ipRequests >= limits.maxRequestsPerIp()) {
            throw tooManyRequests();
        }
    }

    private ApiException tooManyRequests() {
        return new ApiException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Too many sign-in code requests. Please wait a little before trying again."
        );
    }

    private ApiException unauthorized(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, message);
    }

    public record OtpRequestResult(String message, long expiresInSeconds, String devOtp) {
    }

    public record LoginResult(PrepPilotPrincipal user, String rawToken, Instant expiresAt) {
    }
}
