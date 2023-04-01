CREATE TABLE account
(
    id           BIGINT                NOT NULL AUTO_INCREMENT,
    created_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at   TIMESTAMP(6),
    email        VARCHAR(120)          NOT NULL,
    nickname     VARCHAR(20)           NOT NULL,
    password     VARCHAR(61),
    account_type CHAR(6),
    PRIMARY KEY (id)
);

CREATE TABLE `job_category` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name CHAR(16) NOT NULL,
    parent_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (parent_id) REFERENCES job_category (id)
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
    job_category_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT FK_account_id FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_job_category_id FOREIGN KEY (job_category_id) REFERENCES `job_category` (id)
);

CREATE TABLE question
(
    id           BIGINT                NOT NULL AUTO_INCREMENT,
    created_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at   TIMESTAMP(6),
    content      LONGTEXT              NOT NULL,
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

CREATE TABLE `inquiry` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(128) NOT NULL,
    email VARCHAR(120) NOT NULL,
    content VARCHAR(100000) NOT NULL,
    attachedFileUrl VARCHAR(255),
    isAnswered BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);
