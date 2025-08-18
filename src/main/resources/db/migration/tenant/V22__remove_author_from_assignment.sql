-- Remove author column from assignment table since userId already represents the author
ALTER TABLE assignment DROP COLUMN author;