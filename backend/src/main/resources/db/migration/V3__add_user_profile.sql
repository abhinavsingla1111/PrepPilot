ALTER TABLE app_user
    ADD COLUMN full_name  VARCHAR(80),
    ADD COLUMN age        INTEGER,
    ADD COLUMN mobile     VARCHAR(10),
    ADD COLUMN avatar_url  TEXT;
