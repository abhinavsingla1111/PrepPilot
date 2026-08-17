-- Learning-path ordering: a single integer that sorts problems topic-by-topic
-- (following the DSA learning progression) and, within each topic, by difficulty
-- (Easy -> Medium -> Hard). The value is topicRank * 10 + difficultyRank and is
-- (re)computed by the application on startup, so this default is just a placeholder
-- for the existing rows until the seed service backfills them.
ALTER TABLE problem
    ADD COLUMN learning_order INTEGER NOT NULL DEFAULT 0;
