CREATE TABLE schools (
                         id SERIAL PRIMARY KEY,
                         school_id VARCHAR(50) NOT NULL UNIQUE,
                         name VARCHAR(100) NOT NULL,
                         db_url VARCHAR(255) NOT NULL,
                         db_username VARCHAR(100) NOT NULL,
                         db_password VARCHAR(100) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
