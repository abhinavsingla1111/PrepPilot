CREATE TABLE feedback_request (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    subject VARCHAR(120) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    email_status VARCHAR(20) NOT NULL,
    attachment_name VARCHAR(255),
    attachment_content_type VARCHAR(80),
    attachment_size BIGINT,
    attachment_data BYTEA,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT chk_feedback_type CHECK (type IN ('ISSUE', 'IMPROVEMENT')),
    CONSTRAINT chk_feedback_severity CHECK (severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    CONSTRAINT chk_feedback_status CHECK (status IN ('NEW', 'REVIEWING', 'RESOLVED')),
    CONSTRAINT chk_feedback_email_status CHECK (email_status IN ('PENDING', 'SENT', 'FAILED', 'SKIPPED')),
    CONSTRAINT chk_feedback_attachment_size CHECK (attachment_size IS NULL OR attachment_size >= 0)
);

CREATE INDEX idx_feedback_user_created ON feedback_request (user_id, created_at DESC);
CREATE INDEX idx_feedback_status_severity ON feedback_request (status, severity);
