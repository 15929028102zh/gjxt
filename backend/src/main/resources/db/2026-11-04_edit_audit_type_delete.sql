-- 月检 V3: record_edit_audit_type 增加「删除审核」类型
-- 公示期内删除记录走申请+审核通道,审核通过后由后端逻辑删除该记录
-- 取值: NULL=无, 0=回退审核, 1=编辑审核, 2=删除审核
-- 2026-11-04
-- 可重复执行:字段不存在则新增,已存在则只更新注释

SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_monthly_inspection_record' AND COLUMN_NAME='record_edit_audit_type';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_monthly_inspection_record
     ADD COLUMN record_edit_audit_type TINYINT DEFAULT NULL COMMENT '待审核类型: NULL=无, 0=回退审核, 1=编辑审核, 2=删除审核' AFTER record_status",
  "ALTER TABLE sys_monthly_inspection_record
     MODIFY COLUMN record_edit_audit_type TINYINT DEFAULT NULL COMMENT '待审核类型: NULL=无, 0=回退审核, 1=编辑审核, 2=删除审核'");
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;
