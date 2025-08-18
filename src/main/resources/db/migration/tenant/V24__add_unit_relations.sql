-- Add unit_id column to assignment table
ALTER TABLE assignment ADD COLUMN unit_id BIGINT;

-- Add unit_id column to handout table  
ALTER TABLE handout ADD COLUMN unit_id BIGINT;

-- Remove author column from handout table (similar to assignment)
ALTER TABLE handout DROP COLUMN author;

-- Rename user_id to author_id in handout table for consistency
ALTER TABLE handout RENAME COLUMN user_id TO author_id;

-- Add subject_id column to handout table if not exists (for subject relation)
-- Note: subject_id column should already exist, just ensuring FK constraint

-- Add foreign key constraints
ALTER TABLE assignment ADD CONSTRAINT fk_assignment_unit 
    FOREIGN KEY (unit_id) REFERENCES unit(id) ON DELETE SET NULL;

ALTER TABLE handout ADD CONSTRAINT fk_handout_unit 
    FOREIGN KEY (unit_id) REFERENCES unit(id) ON DELETE SET NULL;

ALTER TABLE handout ADD CONSTRAINT fk_handout_subject 
    FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE;