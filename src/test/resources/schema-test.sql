CREATE TABLE account
(
    id           BIGINT                NOT NULL AUTO_INCREMENT,
    created_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at   TIMESTAMP(6),
    email        VARCHAR(120)          NOT NULL,
    nickname     VARCHAR(20)           NOT NULL,
    password     VARCHAR(61)           NOT NULL,
    account_type CHAR,
    PRIMARY KEY (id)
);

CREATE TABLE interview
(
    id           BIGINT                NOT NULL AUTO_INCREMENT,
    created_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at   TIMESTAMP(6),
    title        VARCHAR(128)          NOT NULL,
    account_id   BIGINT                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_account_id FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE question
(
    id           BIGINT                NOT NULL AUTO_INCREMENT,
    created_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at   TIMESTAMP(6),
    content CLOB NOT NULL,
    interview_id BIGINT                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_interview_id FOREIGN KEY (interview_id) REFERENCES interview (id)
);

CREATE TABLE scrap
(
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    created_at   TIMESTAMP(6),
    updated_at   TIMESTAMP(6),
    title        VARCHAR(50) NOT NULL,
    account_id   BIGINT      NOT NULL,
    interview_id BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_account_id_2 FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_interview_id_2 FOREIGN KEY (interview_id) REFERENCES interview (id)
);

CREATE TABLE scrap_question
(
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    content    VARCHAR(1000) NOT NULL,
    scrap_id   BIGINT        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_scrap_id FOREIGN KEY (scrap_id) REFERENCES scrap (id)
);

CREATE TABLE scrap_answer
(
    id                BIGINT NOT NULL AUTO_INCREMENT,
    created_at        TIMESTAMP(6),
    updated_at        TIMESTAMP(6),
    content           VARCHAR(5000),
    scrap_question_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_scrap_question_id FOREIGN KEY (scrap_question_id) REFERENCES scrap_question (id)
);
