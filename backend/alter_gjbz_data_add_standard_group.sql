-- ============================================================
-- 岗检标准表新增 standard_group 字段（可重复执行/幂等）
-- 日期: 2026-09-09
-- 目的:
--   1) gjbz_data 加 standard_group TINYINT NULL
--      取值约定: 1=第一组(生产/炼化) 2=第二组(辅助/公用) 0/NULL=通用
--   2) 为 standard_group 建立索引，便于级联接口按组过滤
--   3) 历史数据保持 NULL（通用），由后续导入脚本按来源文件回填
-- 全部 ALTER 用 INFORMATION_SCHEMA 判断是否已存在，实现幂等。
-- ============================================================
USE `gjxt_inspection`;

-- 1) gjbz_data.standard_group
SELECT COUNT(*) INTO @col FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'gjbz_data'
    AND COLUMN_NAME = 'standard_group';
SET @sql = IF(@col = 0,
  "ALTER TABLE `gjbz_data` ADD COLUMN `standard_group` TINYINT NULL DEFAULT NULL COMMENT '标准组 1=第一组(生产/炼化) 2=第二组(辅助/公用) NULL/0=通用' AFTER `quality`",
  "SELECT 'standard_group exists' AS info");
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2) 索引 idx_standard_group
SELECT COUNT(*) INTO @idx FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'gjbz_data'
    AND INDEX_NAME = 'idx_standard_group';
SET @sql = IF(@idx = 0,
  "ALTER TABLE `gjbz_data` ADD KEY `idx_standard_group` (`standard_group`)",
  "SELECT 'idx_standard_group exists' AS info");
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 验证
SELECT 'gjbz_data.standard_group' AS info,
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'gjbz_data'
       AND COLUMN_NAME = 'standard_group') AS col_ok,
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'gjbz_data'
       AND INDEX_NAME = 'idx_standard_group') AS idx_ok;

-- ============================================================
-- 回滚脚本（单独执行，不要与上面混跑）
-- ============================================================
 ALTER TABLE `gjbz_data` DROP INDEX `idx_standard_group`;
 ALTER TABLE `gjbz_data` DROP COLUMN `standard_group`;
