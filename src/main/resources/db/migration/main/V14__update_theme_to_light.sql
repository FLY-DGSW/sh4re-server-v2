ALTER TABLE users ALTER COLUMN theme SET DEFAULT 'light';
UPDATE users SET theme = 'light' WHERE theme = 'WHITE' or theme = 'white';