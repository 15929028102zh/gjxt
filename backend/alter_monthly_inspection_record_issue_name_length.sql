-- 扩容月检记录表的问题名称字段
-- 背景：issue_name 直接存 gjbz_data.issue 的原文（标准库），2026 版标准最长已达 259 字符，
--       原有 varchar(200) 在录入长标准条目时触发 "Data too long for column 'issue_name'"。
USE `gjxt_inspection`;

ALTER TABLE `sys_monthly_inspection_record`
  MODIFY COLUMN `issue_name` VARCHAR(500) DEFAULT NULL COMMENT '问题名称';

-- 校验：应为 500
SELECT COLUMN_NAME, COLUMN_TYPE, CHARACTER_MAXIMUM_LENGTH
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'gjxt_inspection'
  AND TABLE_NAME = 'sys_monthly_inspection_record'
  AND COLUMN_NAME = 'issue_name';

-- 回滚示例（如需）
-- ALTER TABLE `sys_monthly_inspection_record`
--   MODIFY COLUMN `issue_name` VARCHAR(200) DEFAULT NULL COMMENT '问题名称';
