-- liquibase formatted sql

-- changeSet filippova-t:1
CREATE TABLE notification_task (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT,
    notion TEXT,
    date_time TIMESTAMP WITH TIME ZONE
)

-- changeSet filippova-t:2
DROP TABLE notification_task;

-- changeSet filippova-t:3
CREATE TABLE notification_task (
                                   id SERIAL NOT NULL PRIMARY KEY ,
                                   chat_id BIGINT NOT NULL,
                                   notification_text TEXT NOT NULL,
                                   date_time TIMESTAMP WITH TIME ZONE NOT NULL
)