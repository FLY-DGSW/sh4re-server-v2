-- Remove student column
ALTER TABLE code DROP COLUMN student;

-- Remove className column
ALTER TABLE code DROP COLUMN class_name;

-- Rename user_id to author_id and maintain foreign key
ALTER TABLE code RENAME COLUMN user_id TO author_id;

-- Add class_placement_id column with foreign key
ALTER TABLE code ADD COLUMN class_placement_id BIGINT NOT NULL DEFAULT 1;

-- Add foreign key constraint for class_placement only
-- Note: author_id references main database users table, so no FK constraint here
ALTER TABLE code ADD CONSTRAINT fk_code_class_placement 
    FOREIGN KEY (class_placement_id) REFERENCES class_placement(id) ON DELETE CASCADE;