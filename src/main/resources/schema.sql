CREATE TABLE `account`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `deleted_at` DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at` DATETIME(6),
    `email` CHAR(254) NOT NULL,
    `nickname` CHAR(48) NOT NULL,
    `password` CHAR(61),
    `account_type` CHAR(6),
    `image_url` VARCHAR(300) DEFAULT 'https://interviewbank.s3.us-west-2.amazonaws.com/BasicProfilePhoto.png',
    `password_updated_at` DATETIME(6),
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_account` (`account_type`, `email`)
);

CREATE TABLE `job_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` CHAR(16) NOT NULL,
    `parent_id` BIGINT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`parent_id`) REFERENCES job_category (`id`)
);

CREATE TABLE `interview`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `deleted_at` DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at` DATETIME(6),
    `title` CHAR(128)  NOT NULL,
    `account_id` BIGINT NOT NULL,
    `job_category_id` BIGINT NOT NULL,
    `interview_period` CHAR(32) DEFAULT 'ETC',
    `career_year` CHAR(16) DEFAULT 'ETC',
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`job_category_id`) REFERENCES `job_category` (`id`)
);

CREATE TABLE `question`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `deleted_at` DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at` DATETIME(6),
    `content` TEXT NOT NULL,
    `gpt_answer` TEXT DEFAULT 'GPT가 답변을 생성중입니다.',
    `interview_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`)
);

CREATE TABLE `scrap`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    `title` CHAR(128) NOT NULL,
    `account_id` BIGINT NOT NULL,
    `interview_id` BIGINT NOT NULL,
    `job_category_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`)
);


CREATE TABLE `scrap_question`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    `content` TEXT NOT NULL,
    `scrap_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_id`) REFERENCES `scrap` (`id`)
);

CREATE TABLE `scrap_answer`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    `content` TEXT,
    `scrap_question_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_question_id`) REFERENCES `scrap_question` (`id`)
);

CREATE TABLE `inquiry` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` CHAR(128) NOT NULL,
    `email` CHAR(120) NOT NULL,
    `content` TEXT NOT NULL,
    `attachedFileUrl` CHAR(255),
    `isAnswered` BOOLEAN NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6) NOT NULL
);
