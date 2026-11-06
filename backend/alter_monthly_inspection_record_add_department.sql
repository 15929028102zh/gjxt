-- 月检记录表补充「受检单位」及责任字段（可重复执行）
-- 背景：AddDialog 表单一直提交 departmentId，但实体与表都没有对应字段，导致受检单位从未入库、编辑时无法回写。
USE `gjxt_inspection`;

-- department_id：受检单位（部门ID）
SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_monthly_inspection_record'
      AND COLUMN_NAME = 'department_id') = 0,
  'ALTER TABLE `sys_monthly_inspection_record` ADD COLUMN `department_id` varchar(36) NULL COMMENT ''受检单位（部门ID）'' AFTER `location`',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- responsible_unit：责任单位（历史上由实体使用，此处兜底补齐）
SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_monthly_inspection_record'
      AND COLUMN_NAME = 'responsible_unit') = 0,
  'ALTER TABLE `sys_monthly_inspection_record` ADD COLUMN `responsible_unit` varchar(255) NULL COMMENT ''责任单位'' AFTER `department_id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- responsible_person：责任人
SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_monthly_inspection_record'
      AND COLUMN_NAME = 'responsible_person') = 0,
  'ALTER TABLE `sys_monthly_inspection_record` ADD COLUMN `responsible_person` varchar(255) NULL COMMENT ''责任人'' AFTER `responsible_unit`',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 索引
SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_monthly_inspection_record'
      AND INDEX_NAME = 'idx_department_id') = 0,
  'ALTER TABLE `sys_monthly_inspection_record` ADD KEY `idx_department_id` (`department_id`)',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 历史数据回填：受检单位按创建人所属部门推断，否则非管理员按部门过滤后会查不到任何旧记录
UPDATE `sys_monthly_inspection_record` r
  JOIN `sys_user` u ON r.create_user_id = u.id
  SET r.department_id = u.dept_id
  WHERE r.department_id IS NULL AND u.dept_id IS NOT NULL;

-- 回滚示例（如需）
-- ALTER TABLE `sys_monthly_inspection_record` DROP INDEX `idx_department_id`, DROP COLUMN `department_id`;
