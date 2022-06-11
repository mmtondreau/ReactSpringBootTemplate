
CREATE TABLE auth.session (
    session_id BIGSERIAL PRIMARY KEY,
    token VARCHAR(36) UNIQUE NOT NULL,
    user_id BIGINT references auth.user(user_id) not null,
    expiration timestamp not null
);
