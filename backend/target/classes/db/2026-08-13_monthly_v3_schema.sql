-- ============================================================
-- 月检 V3 重构 Schema 迁移
-- 日期: 2026-08-13
-- 目的:
--   1) sys_dept 加 dept_type(0=运行部门, 1=管理部门)
--   2) sys_monthly_inspection_record 加 check_unit_id/check_unit_name
--   3) sys_monthly_inspection_record 加 nature_modified(0/1)
--   4) sys_monthly_inspection_record 加 modification_snapshot(JSON)
-- 现有部门初始化(用户 2026-08-13 09:03 清单):
--   管理部门(6):生产运行部、科技信息部、机动设备部、安全环保部、综合管理部、控制管理部
--   明确运行(6):乙烯运行部、全密运行部、高密运行部、储运部、公用工程部、运行保障部
--   剩余有效部门:归为运行(0)
-- 全部 ALTER 用 INFORMATION_SCHEMA 判断是否已存在,实现幂等。
-- ============================================================

-- 1) sys_dept.dept_type
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_dept' AND COLUMN_NAME='dept_type';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_dept ADD COLUMN dept_type TINYINT NOT NULL DEFAULT 0 COMMENT '0=运行部门 1=管理部门' AFTER dept_level",
  "SELECT 'dept_type exists' AS info");
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2) 初始化 6 个管理部门为 1
UPDATE sys_dept
  SET dept_type = 1
  WHERE deleted = 0
    AND dept_name IN (
      '生产运行部','科技信息部','机动设备部','安全环保部','综合管理部','控制管理部'
    );
-- 其余有效部门保持默认 0(运行)

-- 3) sys_monthly_inspection_record.check_unit_id + check_unit_name
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection'
    AND TABLE_NAME='sys_monthly_inspection_record'
    AND COLUMN_NAME='check_unit_id';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_monthly_inspection_record
     ADD COLUMN check_unit_id VARCHAR(36) NULL COMMENT '检查单位(管理部门)ID' AFTER department_id,
     ADD COLUMN check_unit_name VARCHAR(100) NULL COMMENT '检查单位名称' AFTER check_unit_id",
  "SELECT 'check_unit_* exists' AS info");
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 4) sys_monthly_inspection_record.nature_modified
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection'
    AND TABLE_NAME='sys_monthly_inspection_record'
    AND COLUMN_NAME='nature_modified';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_monthly_inspection_record
     ADD COLUMN nature_modified TINYINT NOT NULL DEFAULT 0 COMMENT '性质界定是否被改动 0=否 1=是' AFTER quality_name",
  "SELECT 'nature_modified exists' AS info");
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 5) sys_monthly_inspection_record.modification_snapshot
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA='gjxt_inspection'
    AND TABLE_NAME='sys_monthly_inspection_record'
    AND COLUMN_NAME='modification_snapshot';
SET @sql = IF(@col=0,
  "ALTER TABLE sys_monthly_inspection_record
     ADD COLUMN modification_snapshot TEXT NULL COMMENT '原值/修改值 JSON {field:{from,to}}' AFTER modify_description",
  "SELECT 'modification_snapshot exists' AS info");
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 验证
SELECT 'dept_type distribution' AS info;
SELECT dept_type, COUNT(*) AS cnt
  FROM sys_dept WHERE deleted = 0
  GROUP BY dept_type
  ORDER BY dept_type;

SELECT 'record new columns' AS info;
SELECT
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_monthly_inspection_record'
       AND COLUMN_NAME='check_unit_id') AS check_unit_id_ok,
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_monthly_inspection_record'
       AND COLUMN_NAME='check_unit_name') AS check_unit_name_ok,
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_monthly_inspection_record'
       AND COLUMN_NAME='nature_modified') AS nature_modified_ok,
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA='gjxt_inspection' AND TABLE_NAME='sys_monthly_inspection_record'
       AND COLUMN_NAME='modification_snapshot') AS modification_snapshot_ok;

-- ============================================================
-- 回滚脚本(单独执行,不要与上面混跑)
-- ============================================================
-- ALTER TABLE sys_dept DROP COLUMN dept_type;
-- UPDATE sys_dept SET dept_type = 1
--   WHERE deleted = 0 AND dept_name IN ('生产运行部','科技信息部','机动设备部','安全环保部','综合管理部','控制管理部');
-- ALTER TABLE sys_monthly_inspection_record DROP COLUMN check_unit_id;
-- ALTER TABLE sys_monthly_inspection_record DROP COLUMN check_unit_name;
-- ALTER TABLE sys_monthly_inspection_record DROP COLUMN nature_modified;
-- ALTER TABLE sys_monthly_inspection_record DROP COLUMN modification_snapshot;
