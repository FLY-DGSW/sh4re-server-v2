-- Assignment 테이블의 unit_id를 NOT NULL로 변경
-- 기존에 unit_id가 null인 레코드가 있다면 먼저 처리 필요

-- 1. unit_id가 null인 Assignment 확인 및 기본 Unit으로 업데이트 (필요한 경우)
-- UPDATE assignment SET unit_id = (기본 unit id) WHERE unit_id IS NULL;

-- 2. unit_id를 NOT NULL로 변경
ALTER TABLE assignment ALTER COLUMN unit_id SET NOT NULL;

-- 3. 외래키 제약조건이 없다면 추가
-- ALTER TABLE assignment ADD CONSTRAINT fk_assignment_unit 
--   FOREIGN KEY (unit_id) REFERENCES unit(id);