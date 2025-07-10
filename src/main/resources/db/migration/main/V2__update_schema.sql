-- V2__update_schema.sql

-- 1. Drop old tables (optional: if you're resetting)
DROP TABLE IF EXISTS user_refresh_token;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS school;

-- 2. Create updated tables

CREATE TABLE school (
                        id BIGSERIAL PRIMARY KEY,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        name VARCHAR(255) NOT NULL,
                        code VARCHAR(255) NOT NULL
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       grade INTEGER NOT NULL,
                       class_no INTEGER NOT NULL,
                       student_no INTEGER NOT NULL,
                       role VARCHAR(255) DEFAULT 'USER',
                       school_id BIGINT NOT NULL,
                       CONSTRAINT fk_users_school FOREIGN KEY (school_id) REFERENCES school(id)
);

CREATE TABLE user_refresh_token (
                                    id BIGSERIAL PRIMARY KEY,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    username VARCHAR(255),
                                    refresh_token VARCHAR(255),
                                    expiry_date TIMESTAMP
);
