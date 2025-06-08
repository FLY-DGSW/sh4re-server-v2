CREATE TABLE school (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL
);

CREATE TABLE "user" (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    grade INTEGER NOT NULL,
    class_no INTEGER NOT NULL,
    student_no INTEGER NOT NULL,
    role VARCHAR(255) DEFAULT 'USER',
    school_id BIGINT NOT NULL,
    CONSTRAINT fk_user_school FOREIGN KEY (school_id) REFERENCES school(id)
);

CREATE TABLE user_refresh_token (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    username VARCHAR(255),
    refresh_token VARCHAR(255),
    expiry_date TIMESTAMP
);
