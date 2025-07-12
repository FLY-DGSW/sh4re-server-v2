ALTER TABLE users
    ADD COLUMN admission_year INTEGER NOT NULL default extract(year from current_date);