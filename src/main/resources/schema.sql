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
    PRIMARY KEY (`id`)
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

INSERT INTO job_category (id, name, parent_id) VALUES (1, '개발', null);
INSERT INTO job_category (id, name, parent_id) VALUES (2, 'R&D', null);
INSERT INTO job_category (id, name, parent_id) VALUES (3, '디자인', null);
INSERT INTO job_category (id, name, parent_id) VALUES (4, '기획/PM', null);
INSERT INTO job_category (id, name, parent_id) VALUES (5, '마케팅', null);
INSERT INTO job_category (id, name, parent_id) VALUES (6, '기타', null);
INSERT INTO job_category (id, name, parent_id) VALUES (7, '백엔드', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (8, '프런트엔드', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (9, '안드로이드', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (10, 'IOS', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (11, '모바일', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (12, 'DevOps', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (13, 'QA', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (14, '게임', 1);
INSERT INTO job_category (id, name, parent_id) VALUES (15, 'AI', 1);
