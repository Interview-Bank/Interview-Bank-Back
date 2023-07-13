CREATE TABLE account
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at TIMESTAMP(6),
    email CHAR(254) NOT NULL,
    nickname CHAR(48) NOT NULL,
    password CHAR(61),
    account_type CHAR(6),
    image_url VARCHAR(300) DEFAULT 'https://interviewbank.s3.us-west-2.amazonaws.com/BasicProfilePhoto.png',
    password_updated_at TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE job_category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name CHAR(16) NOT NULL,
    parent_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
);

ALTER TABLE job_category
    ADD CONSTRAINT fk_job_category_parent_id
    FOREIGN KEY (parent_id) REFERENCES job_category (id);

CREATE TABLE interview
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at TIMESTAMP(6),
    title CHAR(128) NOT NULL,
    account_id BIGINT NOT NULL,
    job_category_id BIGINT NOT NULL,
    interview_period CHAR(32) DEFAULT 'ETC',
    career_year CHAR(16) DEFAULT 'ETC',
    view BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT FK_account_id FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_job_category_id FOREIGN KEY (job_category_id) REFERENCES job_category (id)
);

CREATE TABLE question
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at TIMESTAMP(6),
    content VARCHAR(1000) NOT NULL,
    gpt_answer VARCHAR(1000) DEFAULT 'GPT가 답변을 생성중입니다.',
    interview_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_interview_id FOREIGN KEY (interview_id) REFERENCES interview (id)
);

CREATE TABLE interview_like
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    deleted_flag BOOLEAN DEFAULT FALSE NOT NULL,
    updated_at TIMESTAMP(6),
    account_id BIGINT NOT NULL,
    interview_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_account_id FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_interview_id FOREIGN KEY (interview_id) REFERENCES interview (id),
    CONSTRAINT uk_account_interview UNIQUE (account_id, interview_id)
);


CREATE TABLE scrap
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    title CHAR(128) NOT NULL,
    account_id BIGINT NOT NULL,
    interview_id BIGINT NOT NULL,
    job_category_id BIGINT NOT NULL,
    is_public BOOLEAN NOT NULL DEFAULT 0,
    view BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT FK_account_id_2 FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_interview_id_2 FOREIGN KEY (interview_id) REFERENCES interview (id)
);

CREATE TABLE scrap_question
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    content VARCHAR(1000) NOT NULL,
    gpt_answer TEXT,
    scrap_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_scrap_id FOREIGN KEY (scrap_id) REFERENCES scrap (id)
);

CREATE TABLE scrap_answer
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    content VARCHAR(1000),
    scrap_question_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_scrap_question_id FOREIGN KEY (scrap_question_id) REFERENCES scrap_question (id)
);

CREATE TABLE `inquiry` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title CHAR(128) NOT NULL,
    email CHAR(120) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    attachedFileUrl CHAR(255),
    isAnswered BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);
