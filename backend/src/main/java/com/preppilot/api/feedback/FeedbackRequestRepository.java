package com.preppilot.api.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, UUID> {
}
