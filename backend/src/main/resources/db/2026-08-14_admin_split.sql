-- 管理员权限拆分:is_admin → is_weekly_admin + is_monthly_admin
-- 迁移策略:原 is_admin=1 → is_weekly_admin=1, is_monthly_admin=0
-- is_admin 列保留(兼容老 token),后续版本再 DROP

-- 1) 加 is_weekly_admin 列
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_user' AND COLUMN_NAME='is_weekly_admin';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_user ADD COLUMN is_weekly_admin TINYINT NOT NULL DEFAULT 0 COMMENT '周检管理员 0=否 1=是' AFTER is_admin",
  "SELECT 'is_weekly_admin exists'");
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- 2) 加 is_monthly_admin 列
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_user' AND COLUMN_NAME='is_monthly_admin';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_user ADD COLUMN is_monthly_admin TINYINT NOT NULL DEFAULT 0 COMMENT '月检管理员 0=否 1=是' AFTER is_weekly_admin",
  "SELECT 'is_monthly_admin exists'");
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- 3) 数据迁移:is_admin=1 → is_weekly_admin=1, is_monthly_admin 留 0
UPDATE sys_user SET is_weekly_admin = 1 WHERE is_admin = 1 AND deleted = 0;