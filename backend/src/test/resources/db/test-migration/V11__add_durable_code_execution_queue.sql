ALTER TABLE coding_execution_submission ADD COLUMN attempt_count INTEGER NOT NULL DEFAULT 0;
ALTER TABLE coding_execution_submission ADD COLUMN available_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE coding_execution_submission ADD COLUMN lease_token_hash VARCHAR(64);
ALTER TABLE coding_execution_submission ADD COLUMN lease_expires_at TIMESTAMP WITH TIME ZONE;

UPDATE coding_execution_submission
SET available_at = created_at
WHERE available_at IS NULL;

ALTER TABLE coding_execution_submission ALTER COLUMN available_at SET NOT NULL;

ALTER TABLE coding_execution_submission
    ADD CONSTRAINT chk_execution_attempts CHECK (attempt_count BETWEEN 0 AND 10);

CREATE INDEX idx_execution_claim
    ON coding_execution_submission (status, available_at, lease_expires_at, created_at);
