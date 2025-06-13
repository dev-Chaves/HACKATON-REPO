CREATE INDEX idx_benefit_supplier_enterprise ON tb_benefit(supplier_enterprise_id);

CREATE INDEX idx_benefit_category_supplier ON tb_benefit(category, supplier_enterprise_id);