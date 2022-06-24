CREATE SCHEMA auth;
CREATE TABLE auth.roles
(
    role_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE auth.authorities (
    authority_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);


CREATE TABLE auth.role_authorities (
    role_authority_id BIGSERIAL PRIMARY KEY,
    role_id BIGINT references auth.roles(role_id) NOT NULL,
    authority_id BIGINT references auth.authorities(authority_id) NOT NULL
);

CREATE TABLE auth.users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) UNIQUE NOT NULL,
    password_enc VARCHAR(512) NOT NULL,
    api_key VARCHAR(36) NOT NULL,
    account_non_expired BOOLEAN default TRUE,
    account_non_locked BOOLEAN default TRUE,
    credentials_non_expired BOOLEAN default TRUE,
    enabled BOOLEAN default TRUE,
    role_id BIGINT references auth.roles(role_id) not null
);

CREATE VIEW auth.vw_user AS
SELECT user_id, username, password_enc, account_non_expired,
       account_non_locked, credentials_non_expired, enabled,
       r.name as role, a.name as authority
from auth.users u
         JOIN auth.roles r on u.role_id = r.role_id
         JOIN auth.role_authorities ra on r.role_id = ra.role_id
         JOIN auth.authorities a on ra.authority_id = a.authority_id