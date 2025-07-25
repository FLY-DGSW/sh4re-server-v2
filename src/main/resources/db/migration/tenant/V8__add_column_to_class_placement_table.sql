ALTER TABLE class_placement
    ADD COLUMN student_number INT DEFAULT 0;

ALTER TABLE class_placement
    ALTER COLUMN student_number SET NOT NULL;