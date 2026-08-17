package com.preppilot.api.feedback;

import com.preppilot.api.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "feedback_request")
public class FeedbackRequest {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackSeverity severity;

    @Column(nullable = false, length = 120)
    private String subject;

    @Column(nullable = false, length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false, length = 20)
    private FeedbackEmailStatus emailStatus;

    @Column(name = "attachment_name", length = 255)
    private String attachmentName;

    @Column(name = "attachment_content_type", length = 80)
    private String attachmentContentType;

    @Column(name = "attachment_size")
    private Long attachmentSize;

    @Column(name = "attachment_data", columnDefinition = "bytea")
    private byte[] attachmentData;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected FeedbackRequest() {
    }

    public FeedbackRequest(
            UUID id,
            AppUser user,
            FeedbackType type,
            FeedbackSeverity severity,
            String subject,
            String description,
            Instant createdAt
    ) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.severity = severity;
        this.subject = subject;
        this.description = description;
        this.status = FeedbackStatus.NEW;
        this.emailStatus = FeedbackEmailStatus.PENDING;
        this.createdAt = createdAt;
    }

    public void attach(String name, String contentType, byte[] data) {
        this.attachmentName = name;
        this.attachmentContentType = contentType;
        this.attachmentSize = (long) data.length;
        this.attachmentData = data.clone();
    }

    public void markEmailStatus(FeedbackEmailStatus newStatus) {
        this.emailStatus = newStatus;
    }

    public UUID getId() { return id; }
    public AppUser getUser() { return user; }
    public FeedbackType getType() { return type; }
    public FeedbackSeverity getSeverity() { return severity; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public FeedbackStatus getStatus() { return status; }
    public FeedbackEmailStatus getEmailStatus() { return emailStatus; }
    public String getAttachmentName() { return attachmentName; }
    public String getAttachmentContentType() { return attachmentContentType; }
    public Long getAttachmentSize() { return attachmentSize; }
    public byte[] getAttachmentData() { return attachmentData == null ? null : attachmentData.clone(); }
    public Instant getCreatedAt() { return createdAt; }
}
