-- code 컬럼이 없다면 추가
ALTER TABLE schools
    ADD COLUMN code VARCHAR(50) NOT NULL DEFAULT 'NO-CODE';

-- code는 고유해야 하므로 unique 제약 추가 (기존 데이터가 안전하다는 가정 하에)
ALTER TABLE schools
    ADD CONSTRAINT uq_schools_code UNIQUE (code);
