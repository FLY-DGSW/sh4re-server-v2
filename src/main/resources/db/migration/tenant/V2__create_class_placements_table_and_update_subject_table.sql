CREATE TABLE class_placements (
                        id SERIAL PRIMARY KEY,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        userId BIGINT NOT NULL,
                        schoolYear INTEGER NOT NULL,
                        grade INTEGER NOT NULL,
                        classNumber INTEGER NOT NULL
);

ALTER TABLE IF EXISTS subject
    ADD COLUMN description VARCHAR(255) NOT NULL DEFAULT 'description',
    ADD COLUMN school_year INTEGER NOT NULL DEFAULT extract(year from current_date),
    ADD COLUMN class_number INTEGER NOT NULL DEFAULT 0;

