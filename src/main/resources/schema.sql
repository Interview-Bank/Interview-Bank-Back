CREATE TABLE `account`
(
    `id`                  BIGINT                 NOT NULL AUTO_INCREMENT,
    `created_at`          DATETIME(6),
    `deleted_at`          DATETIME(6),
    `deleted_flag`        BIT          DEFAULT 0 NOT NULL,
    `updated_at`          DATETIME(6),
    `email`               CHAR(254)              NOT NULL,
    `nickname`            CHAR(48)               NOT NULL,
    `password`            CHAR(61),
    `account_type`        CHAR(6),
    `image_url`           VARCHAR(300) DEFAULT 'https://interviewbank.s3.us-west-2.amazonaws.com/BasicProfilePhoto.png',
    `password_updated_at` DATETIME(6),
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_account` (`account_type`, `email`)
);

CREATE TABLE `job_category`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `name`       CHAR(16) NOT NULL,
    `parent_id`  BIGINT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`parent_id`) REFERENCES job_category (`id`)
);

CREATE TABLE `interview`
(
    `id`               BIGINT    NOT NULL AUTO_INCREMENT,
    `created_at`       DATETIME(6),
    `deleted_at`       DATETIME(6),
    `deleted_flag`     BIT                DEFAULT 0 NOT NULL,
    `updated_at`       DATETIME(6),
    `title`            CHAR(128) NOT NULL,
    `account_id`       BIGINT    NOT NULL,
    `job_category_id`  BIGINT    NOT NULL,
    `interview_period` CHAR(32)           DEFAULT 'ETC',
    `career_year`      CHAR(16)           DEFAULT 'ETC',
    `view`             BIGINT    NOT NULL DEFAULT 0,
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
    `content`      TEXT          NOT NULL,
    `gpt_answer`   TEXT,
    `interview_id` BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`)
);

CREATE TABLE `interview_like`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6),
    `deleted_at`   DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at`   DATETIME(6),
    `account_id`   BIGINT        NOT NULL,
    `interview_id` BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`),
    CONSTRAINT `uk_account_interview` UNIQUE (`account_id`, `interview_id`)
);

CREATE TABLE `scrap`
(
    `id`              BIGINT    NOT NULL AUTO_INCREMENT,
    `created_at`      DATETIME(6),
    `updated_at`      DATETIME(6),
    `title`           CHAR(128) NOT NULL,
    `account_id`      BIGINT    NOT NULL,
    `interview_id`    BIGINT    NOT NULL,
    `job_category_id` BIGINT    NOT NULL,
    `is_public`       BOOLEAN   NOT NULL DEFAULT 0,
    `view`            BIGINT    NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`)
);


CREATE TABLE `scrap_question`
(
    `id`         BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6),
    `updated_at` DATETIME(6),
    `content`    TEXT   NOT NULL,
    `gpt_answer` TEXT,
    `scrap_id`   BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_id`) REFERENCES `scrap` (`id`)
);

CREATE TABLE `scrap_answer`
(
    `id`                BIGINT NOT NULL AUTO_INCREMENT,
    `created_at`        DATETIME(6),
    `updated_at`        DATETIME(6),
    `content`           TEXT,
    `scrap_question_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_question_id`) REFERENCES `scrap_question` (`id`)
);

CREATE TABLE `scrap_like`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6),
    `deleted_at`   DATETIME(6),
    `deleted_flag` BIT DEFAULT 0 NOT NULL,
    `updated_at`   DATETIME(6),
    `account_id`   BIGINT        NOT NULL,
    `scrap_id`     BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT FOREIGN KEY (`scrap_id`) REFERENCES `interview` (`id`),
    CONSTRAINT `uk_account_scrap` UNIQUE (`account_id`, `scrap_id`)
);

CREATE TABLE `inquiry`
(
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title`             CHAR(128) NOT NULL,
    `email`             CHAR(120) NOT NULL,
    `content`           TEXT      NOT NULL,
    `attached_file_url` CHAR(255),
    `is_answered`       BOOLEAN   NOT NULL,
    `created_at`        DATETIME(6) NOT NULL,
    `updated_at`        DATETIME(6) NOT NULL
);

CREATE TABLE `temporary_interview`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT,
    `title`            VARCHAR(128),
    `account_id`       BIGINT NOT NULL,
    `job_category_id`  BIGINT,
    `interview_period` CHAR(32) DEFAULT 'ETC',
    `career_year`      CHAR(16) DEFAULT 'ETC', -- Enum values should be replaced with your actual values
    `created_at`       DATETIME(6) NOT NULL,
    `updated_at`       DATETIME(6) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    FOREIGN KEY (`job_category_id`) REFERENCES `job_category` (`id`)
);

CREATE TABLE `temporary_question`
(
    `id`                     BIGINT NOT NULL AUTO_INCREMENT,
    `content`                TEXT,
    `temporary_interview_id` BIGINT NOT NULL,
    `created_at`             DATETIME(6) NOT NULL,
    `updated_at`             DATETIME(6) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`temporary_interview_id`) REFERENCES `temporary_interview` (`id`)
);

CREATE TABLE BATCH_JOB_INSTANCE  (
                                     JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
                                     VERSION BIGINT ,
                                     JOB_NAME VARCHAR(100) NOT NULL,
                                     JOB_KEY VARCHAR(32) NOT NULL,
                                     constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION  (
                                      JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                      VERSION BIGINT  ,
                                      JOB_INSTANCE_ID BIGINT NOT NULL,
                                      CREATE_TIME DATETIME(6) NOT NULL,
                                      START_TIME DATETIME(6) DEFAULT NULL ,
                                      END_TIME DATETIME(6) DEFAULT NULL ,
                                      STATUS VARCHAR(10) ,
                                      EXIT_CODE VARCHAR(2500) ,
                                      EXIT_MESSAGE VARCHAR(2500) ,
                                      LAST_UPDATED DATETIME(6),
                                      JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
                                      constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
                                          references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
                                             JOB_EXECUTION_ID BIGINT NOT NULL ,
                                             TYPE_CD VARCHAR(6) NOT NULL ,
                                             KEY_NAME VARCHAR(100) NOT NULL ,
                                             STRING_VAL VARCHAR(250) ,
                                             DATE_VAL DATETIME(6) DEFAULT NULL ,
                                             LONG_VAL BIGINT ,
                                             DOUBLE_VAL DOUBLE PRECISION ,
                                             IDENTIFYING CHAR(1) NOT NULL ,
                                             constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                                 references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION  (
                                       STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                       VERSION BIGINT NOT NULL,
                                       STEP_NAME VARCHAR(100) NOT NULL,
                                       JOB_EXECUTION_ID BIGINT NOT NULL,
                                       START_TIME DATETIME(6) NOT NULL ,
                                       END_TIME DATETIME(6) DEFAULT NULL ,
                                       STATUS VARCHAR(10) ,
                                       COMMIT_COUNT BIGINT ,
                                       READ_COUNT BIGINT ,
                                       FILTER_COUNT BIGINT ,
                                       WRITE_COUNT BIGINT ,
                                       READ_SKIP_COUNT BIGINT ,
                                       WRITE_SKIP_COUNT BIGINT ,
                                       PROCESS_SKIP_COUNT BIGINT ,
                                       ROLLBACK_COUNT BIGINT ,
                                       EXIT_CODE VARCHAR(2500) ,
                                       EXIT_MESSAGE VARCHAR(2500) ,
                                       LAST_UPDATED DATETIME(6),
                                       constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
                                           references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
                                               STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                               SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                               SERIALIZED_CONTEXT TEXT ,
                                               constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                                   references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
                                              JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT ,
                                              constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                                  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (
                                          ID BIGINT NOT NULL,
                                          UNIQUE_KEY CHAR(1) NOT NULL,
                                          constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ (
                                         ID BIGINT NOT NULL,
                                         UNIQUE_KEY CHAR(1) NOT NULL,
                                         constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ (
                               ID BIGINT NOT NULL,
                               UNIQUE_KEY CHAR(1) NOT NULL,
                               constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);
