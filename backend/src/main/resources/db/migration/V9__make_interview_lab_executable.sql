ALTER TABLE coding_interview_prompt ADD COLUMN active BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE coding_interview_prompt ADD COLUMN company_tags VARCHAR(300) NOT NULL DEFAULT '';
ALTER TABLE coding_interview_prompt ADD COLUMN input_format TEXT NOT NULL DEFAULT '';
ALTER TABLE coding_interview_prompt ADD COLUMN output_format TEXT NOT NULL DEFAULT '';
ALTER TABLE coding_interview_prompt ADD COLUMN starter_java TEXT NOT NULL DEFAULT '';
ALTER TABLE coding_interview_prompt ADD COLUMN starter_cpp TEXT NOT NULL DEFAULT '';
ALTER TABLE coding_interview_prompt ADD COLUMN starter_python TEXT NOT NULL DEFAULT '';
ALTER TABLE coding_interview_prompt ADD COLUMN public_tests_json TEXT NOT NULL DEFAULT '[]';
ALTER TABLE coding_interview_prompt ADD COLUMN hidden_tests_json TEXT NOT NULL DEFAULT '[]';

ALTER TABLE coding_interview_session DROP CONSTRAINT chk_coding_session_language;
UPDATE coding_interview_session SET language = 'JAVA' WHERE language = 'JAVASCRIPT';
ALTER TABLE coding_interview_session
    ADD CONSTRAINT chk_coding_session_language CHECK (language IN ('JAVA', 'PYTHON', 'CPP'));

ALTER TABLE coding_interview_session DROP COLUMN test_cases;
ALTER TABLE coding_interview_session DROP COLUMN rubric_code_quality;
ALTER TABLE coding_interview_session DROP COLUMN rubric_testing;
ALTER TABLE coding_interview_session DROP COLUMN rubric_communication;

CREATE TABLE coding_execution_submission (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    session_id UUID NOT NULL REFERENCES coding_interview_session(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL,
    language VARCHAR(20) NOT NULL,
    source_code TEXT NOT NULL,
    total_tests INTEGER NOT NULL,
    passed_tests INTEGER NOT NULL DEFAULT 0,
    results_json TEXT NOT NULL DEFAULT '[]',
    failure_message VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT chk_execution_status CHECK (status IN ('QUEUED', 'RUNNING', 'PASSED', 'FAILED', 'ERROR')),
    CONSTRAINT chk_execution_language CHECK (language IN ('JAVA', 'PYTHON', 'CPP')),
    CONSTRAINT chk_execution_total CHECK (total_tests BETWEEN 1 AND 20),
    CONSTRAINT chk_execution_passed CHECK (passed_tests BETWEEN 0 AND 20)
);

CREATE INDEX idx_execution_user_created
    ON coding_execution_submission (user_id, created_at DESC);

CREATE INDEX idx_execution_session_created
    ON coding_execution_submission (session_id, created_at DESC);
