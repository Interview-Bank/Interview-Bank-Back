CREATE TABLE `account`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6),
    `deleted_at`   DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at`   DATETIME(6),
    `email`        VARCHAR(120)  NOT NULL,
    `nickname`     VARCHAR(20)   NOT NULL,
    `password`     VARCHAR(61)   NOT NULL,
    `account_type` CHAR(6),
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_account` (`account_type`, `email`)
);

CREATE TABLE `job_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` CHAR(16) NOT NULL,
    `parent_id` BIGINT,
    `created_at` DATETIME,
    `updated_at` DATETIME,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`parent_id`) REFERENCES job_category (`id`)
);

CREATE TABLE `interview`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6),
    `deleted_at`   DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at`   DATETIME(6),
    `title`        VARCHAR(128)  NOT NULL,
    `account_id`   BIGINT        NOT NULL,
    `job_category_id` BIGINT,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`job_category_id`) REFERENCES `job_category` (`id`)
);

CREATE TABLE `question`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6),
    `deleted_at`   DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at`   DATETIME(6),
    `content`      LONGTEXT      NOT NULL,
    `interview_id` BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`)
);

CREATE TABLE `scrap`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6),
    `updated_at`   DATETIME(6),
    `title`        VARCHAR(50) NOT NULL,
    `account_id`   BIGINT      NOT NULL,
    `interview_id` BIGINT      NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`)
);


CREATE TABLE `scrap_question`
(
    `id`         BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    `content`    VARCHAR(1000) NOT NULL,
    `scrap_id`   BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_id`) REFERENCES `scrap` (`id`)
);

CREATE TABLE `scrap_answer`
(
    `id`                BIGINT NOT NULL AUTO_INCREMENT,
    `created_at`        DATETIME(6),
    `updated_at`        DATETIME(6),
    `content`           VARCHAR(5000),
    `scrap_question_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_question_id`) REFERENCES `scrap_question` (`id`)
);

CREATE TABLE `inquiry` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(128) NOT NULL,
    `email` VARCHAR(120) NOT NULL,
    `content` VARCHAR(100000) NOT NULL,
    `attachedFileUrl` VARCHAR(255),
    `isAnswered` BOOLEAN NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6) NOT NULL
);
