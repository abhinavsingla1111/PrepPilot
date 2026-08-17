package com.preppilot.api.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface OtpChallengeRepository extends JpaRepository<OtpChallenge, UUID> {
    Optional<OtpChallenge> findFirstByEmailOrderByCreatedAtDesc(String email);

    long countByEmailAndCreatedAtGreaterThanEqual(String email, Instant createdAt);

    long countByRequestIpHashAndCreatedAtGreaterThanEqual(String requestIpHash, Instant createdAt);
}
