-- 插入考核情况字典数据
INSERT INTO sys_dictionary (id, dict_type, dict_label, dict_value, dict_code, description, status, sort_order, create_time, update_time, deleted) VALUES
('assessment_001', '考核情况', '合格', '合格', 'QUALIFIED', '考核结果为合格', 1, 1, NOW(), NOW(), 0),
('assessment_002', '考核情况', '不合格', '不合格', 'UNQUALIFIED', '考核结果为不合格', 1, 2, NOW(), NOW(), 0),
('assessment_003', '考核情况', '优秀', '优秀', 'EXCELLENT', '考核结果为优秀', 1, 3, NOW(), NOW(), 0),
('assessment_004', '考核情况', '良好', '良好', 'GOOD', '考核结果为良好', 1, 4, NOW(), NOW(), 0),
('assessment_005', '考核情况', '待评定', '待评定', 'PENDING', '考核结果待评定', 1, 5, NOW(), NOW(), 0);