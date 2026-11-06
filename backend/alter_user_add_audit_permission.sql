-- ============================================================
-- 用户表 sys_user 新增月检审核权限字段
-- 适用：让"月检编辑/回退审核通知对象"和"审核通过/拒绝权限"统一
--       由 audit_permission 控制，与 is_admin/monthly_permission 解耦
--
-- 字段语义：
--   audit_permission TINYINT NOT NULL DEFAULT 0
--     0 = 否（不接收月检审核通知，不能审核通过/拒绝）
--     1 = 是（接收月检审核通知，可审核通过/拒绝）
--
-- 默认值：0（不允许审核）。管理员需在「用户管理」弹框手动开启。
-- ============================================================

USE gjxt_inspection;

-- 新增字段（已存在则跳过）
SET @db := DATABASE();

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_user' AND COLUMN_NAME = 'audit_permission') = 0,
  'ALTER TABLE sys_user ADD COLUMN audit_permission TINYINT NOT NULL DEFAULT 0 COMMENT ''月检审核权限：0=否，1=是'' AFTER monthly_permission',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 验证字段已生效
SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @db
  AND TABLE_NAME = 'sys_user'
  AND COLUMN_NAME = 'audit_permission';