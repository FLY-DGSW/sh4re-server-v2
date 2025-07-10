DROP TABLE IF EXISTS school CASCADE;

-- tenant_id 컬럼 추가
ALTER TABLE schools
    ADD COLUMN tenant_id VARCHAR(50) NOT NULL DEFAULT 'default-tenant';

-- tenant_id 컬럼도 고유하게 하고 싶다면 아래도 추가 (선택)
ALTER TABLE schools
    ADD CONSTRAINT uq_schools_tenant_id UNIQUE (tenant_id);