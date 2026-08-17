package com.preppilot.api.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodingInterviewSessionRepository extends JpaRepository<CodingInterviewSession, UUID> {

    Optional<CodingInterviewSession> findByIdAndUserId(UUID id, UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select session from CodingInterviewSession session where session.id = :id and session.user.id = :userId")
    Optional<CodingInterviewSession> findByIdAndUserIdForUpdate(UUID id, UUID userId);

    Optional<CodingInterviewSession> findFirstByUserIdAndStatusOrderByStartedAtDesc(
            UUID userId,
            CodingInterviewStatus status
    );

    List<CodingInterviewSession> findAllByUserIdOrderByStartedAtDesc(UUID userId);

    @Query("select distinct session.prompt.id from CodingInterviewSession session where session.user.id = :userId")
    List<Long> findUsedPromptIds(UUID userId);
}
