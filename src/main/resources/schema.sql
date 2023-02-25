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
    PRIMARY KEY (`id`)
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
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
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
