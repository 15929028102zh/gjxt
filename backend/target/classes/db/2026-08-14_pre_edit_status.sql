-- 月检 V3: 加 pre_edit_status 字段,记录提交编辑/回退审核前的原状态
-- 审核通过/拒绝后,recordStatus 恢复到 pre_edit_status(而不是 4 或 1)
-- 2026-08-14

SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_monthly_inspection_record' AND COLUMN_NAME='pre_edit_status';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_monthly_inspection_record
     ADD COLUMN pre_edit_status TINYINT NULL COMMENT '提交审核前的原状态(0/1/2),审核后恢复' AFTER record_edit_audit_type",
  "SELECT 'pre_edit_status exists'");
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;