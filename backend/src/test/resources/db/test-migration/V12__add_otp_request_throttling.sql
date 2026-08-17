ALTER TABLE otp_challenge
    ADD COLUMN request_ip_hash VARCHAR(64);

UPDATE otp_challenge
SET request_ip_hash = REPEAT('0', 64)
WHERE request_ip_hash IS NULL;

ALTER TABLE otp_challenge
    ALTER COLUMN request_ip_hash SET NOT NULL;

CREATE INDEX idx_otp_challenge_ip_created
    ON otp_challenge (request_ip_hash, created_at DESC);
