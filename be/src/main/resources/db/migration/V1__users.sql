CREATE SCHEMA auth;
CREATE TABLE auth.roles
(
    role_id BIGSERIAL PRIMARY KEY,
    name    VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE auth.authorities
(
    authority_id BIGSERIAL PRIMARY KEY,
    name         VARCHAR(32) NOT NULL UNIQUE
);


CREATE TABLE auth.role_authorities
(
    role_authority_id BIGSERIAL PRIMARY KEY,
    role_id           BIGINT references auth.roles (role_id)            NOT NULL,
    authority_id      BIGINT references auth.authorities (authority_id) NOT NULL
);

CREATE INDEX auth_role_authorities_role_id_index on auth.role_authorities (role_id);

CREATE TABLE auth.user_roles
(
    user_role_id BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    role_id      BIGINT NOT NULL,
    unique (user_id, role_id)
);

CREATE INDEX auth_user_roles_user_id_index on auth.user_roles (user_id);

CREATE TABLE auth.users
(
    user_id                 BIGSERIAL PRIMARY KEY,
    username                VARCHAR(64) UNIQUE NOT NULL,
    password_enc            VARCHAR(512)       NOT NULL,
    account_non_expired     BOOLEAN default TRUE,
    account_non_locked      BOOLEAN default TRUE,
    credentials_non_expired BOOLEAN default TRUE,
    enabled                 BOOLEAN default TRUE
);

CREATE VIEW auth.vw_user AS
SELECT u.user_id,
       username,
       password_enc,
       account_non_expired,
       account_non_locked,
       credentials_non_expired,
       enabled,
       r.name as role,
       a.name as authority
from auth.users u
         JOIN auth.user_roles ur on ur.user_id = u.user_id
         JOIN auth.roles r on ur.role_id = r.role_id
         JOIN auth.role_authorities ra on r.role_id = ra.role_id
         JOIN auth.authorities a on ra.authority_id = a.authority_id