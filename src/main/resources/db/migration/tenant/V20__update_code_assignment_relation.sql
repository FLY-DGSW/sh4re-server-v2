-- Drop existing assignment column (String type)
ALTER TABLE code DROP COLUMN assignment;

-- Add new assignment_id column with foreign key reference
ALTER TABLE code ADD COLUMN assignment_id BIGINT;

-- Add foreign key constraint
ALTER TABLE code ADD CONSTRAINT fk_code_assignment 
    FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE SET NULL;