-- Mirror of production V10: widen the assessment topic allow-list for OS.
ALTER TABLE assessment_question DROP CONSTRAINT chk_assessment_question_topic;
ALTER TABLE assessment_question ADD CONSTRAINT chk_assessment_question_topic CHECK (
    topic IN (
        'OOPS', 'JAVA', 'SPRING_BOOT', 'REACT', 'CPP', 'PYTHON',
        'COMPUTER_NETWORKS', 'MYSQL', 'OPERATING_SYSTEMS'
    )
);

ALTER TABLE assessment_attempt DROP CONSTRAINT chk_assessment_attempt_topic;
ALTER TABLE assessment_attempt ADD CONSTRAINT chk_assessment_attempt_topic CHECK (
    topic IN (
        'OOPS', 'JAVA', 'SPRING_BOOT', 'REACT', 'CPP', 'PYTHON',
        'COMPUTER_NETWORKS', 'MYSQL', 'OPERATING_SYSTEMS'
    )
);
