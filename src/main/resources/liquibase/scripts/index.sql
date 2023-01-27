--liquibase formatted sql

--changeset dfetisov:1
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='chat'
--onFail=MARK_RAN
CREATE TABLE chat
(
    id            BIGINT PRIMARY KEY generated always as identity,
    name          TEXT NOT NULL,
    telegram_name TEXT NOT NULL,
    phone         VARCHAR(15),
    is_volunteer  BOOLEAN DEFAULT FALSE
);

--changeset dfetisov:2
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='call_request'
--onFail=MARK_RAN
CREATE TABLE call_request
(
    id                    BIGINT PRIMARY KEY generated always as identity,
    id_chat_client        BIGINT    NOT NULL REFERENCES chat (id),
    id_chat_volunteer     BIGINT REFERENCES chat (id),
    is_open               BOOLEAN DEFAULT TRUE,
    local_date_time_open  TIMESTAMP NOT NULL,
    local_date_time_close TIMESTAMP
);

--changeset zaytsev:3
--precondition-sql-check expectedResult:1 SELECT count(*) FROM pg_tables WHERE tablename='chat'
--onFail=MARK_RAN
alter table chat
    alter column id drop identity;

--changeset dfetisov:4
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='cat_owner'
--onFail=MARK_RAN
CREATE TABLE cat_owner
(
    id                   BIGINT PRIMARY KEY,
    owner_name           TEXT      NOT NULL,
    cat_name             TEXT      NOT NULL,
    start_date           TIMESTAMP NOT NULL,
    day_to_end_reporting BIGINT    NOT NULL DEFAULT 30,
    chat_id              BIGINT    NOT NULL REFERENCES chat (id)
);

--changeset dfetisov:5
--precondition-sql-check expectedResult:1 SELECT count(*) FROM pg_tables WHERE tablename='chat'
--onFail=MARK_RAN
ALTER TABLE chat
    ADD COLUMN is_owner BOOLEAN DEFAULT FALSE;

--changeset dfetisov:6
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='report_cat_owner'
--onFail=MARK_RAN
CREATE TABLE report_cat_owner
(
    id              BIGINT PRIMARY KEY generated always as identity,
    chat_id         BIGINT    NOT NULL,
    time            TIMESTAMP NOT NULL,
    completed_today BOOLEAN DEFAULT FALSE,
    text            TEXT      NOT NULL,
    file_path       TEXT      NOT NULL,
    cat_owner_id    BIGINT    NOT NULL REFERENCES cat_owner (id)
);

--changeset dfetisov:7
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='dog_owner'
--onFail=MARK_RAN
CREATE TABLE dog_owner
(
    id                   BIGINT PRIMARY KEY,
    owner_name           TEXT      NOT NULL,
    dog_name             TEXT      NOT NULL,
    start_date           TIMESTAMP NOT NULL,
    day_to_end_reporting BIGINT    NOT NULL DEFAULT 30,
    chat_id              BIGINT    NOT NULL REFERENCES chat (id)
);
--changeset dfetisov:8
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='report_dog_owner'
--onFail=MARK_RAN
CREATE TABLE report_dog_owner
(
    id              BIGINT PRIMARY KEY generated always as identity,
    chat_id         BIGINT    NOT NULL,
    time            TIMESTAMP NOT NULL,
    completed_today BOOLEAN DEFAULT FALSE,
    text            TEXT      NOT NULL,
    file_path       TEXT      NOT NULL,
    dog_owner_id    BIGINT    NOT NULL REFERENCES dog_owner (id)
);

