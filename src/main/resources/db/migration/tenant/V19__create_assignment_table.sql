CREATE TABLE assignment (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    input_example TEXT,
    output_example TEXT,
    deadline TIMESTAMP NOT NULL,
    subject_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assignment_subject FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE
);