ALTER TABLE IF EXISTS class_placements
    RENAME TO class_placment;

ALTER TABLE IF EXISTS subject
    RENAME COlUMN class_number TO grade;