ALTER TABLE users ALTER COLUMN theme SET DEFAULT 'LIGHT';
UPDATE users SET theme = 'LIGHT' WHERE theme = 'light'