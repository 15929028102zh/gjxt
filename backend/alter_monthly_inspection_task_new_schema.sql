-- ============================================================
-- 月检任务表 sys_monthly_inspection_task 新结构迁移
-- 适用：后端 2024-08-06 代码调整
-- 变更要点：
--   1) 新增字段：task_name, inspection_start_time, inspection_end_time,
--                publicity_start_time, publicity_end_time
--   2) 删除字段：inspection_time, group_leader_id, deputy_leader_id,
--                department_id, profession_id, issue, quality_definition,
--                safety_score, issue_description, issue_image_url,
--                inspector, rectification_deadline, task_status
--   3) 删除冗余索引，新增时间字段索引
-- ============================================================

USE gjxt_inspection;

-- ---------- 1. 新增字段（已存在则跳过） ----------
SET @db := DATABASE();

-- task_name
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'task_name') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD COLUMN task_name varchar(200) DEFAULT NULL COMMENT ''任务名称'' AFTER task_no',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- inspection_start_time
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'inspection_start_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD COLUMN inspection_start_time datetime DEFAULT NULL COMMENT ''月检开始时间'' AFTER task_name',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- inspection_end_time
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'inspection_end_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD COLUMN inspection_end_time datetime DEFAULT NULL COMMENT ''月检结束时间'' AFTER inspection_start_time',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- publicity_start_time
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'publicity_start_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD COLUMN publicity_start_time datetime DEFAULT NULL COMMENT ''公示开始时间'' AFTER inspection_end_time',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- publicity_end_time
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'publicity_end_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD COLUMN publicity_end_time datetime DEFAULT NULL COMMENT ''公示结束时间'' AFTER publicity_start_time',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 2. 清理旧字段（不存在则跳过） ----------
-- inspection_time
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'inspection_time') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN inspection_time',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- group_leader_id
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'group_leader_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN group_leader_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- deputy_leader_id
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'deputy_leader_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN deputy_leader_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- department_id
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'department_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN department_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- profession_id
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'profession_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN profession_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- issue
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'issue') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN issue',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- quality_definition
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'quality_definition') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN quality_definition',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- safety_score
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'safety_score') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN safety_score',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- issue_description
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'issue_description') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN issue_description',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- issue_image_url
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'issue_image_url') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN issue_image_url',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- inspector
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'inspector') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN inspector',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- rectification_deadline
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'rectification_deadline') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN rectification_deadline',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- task_status（与 status 字段不同，是旧版本遗留的冗余字段）
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND COLUMN_NAME = 'task_status') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP COLUMN task_status',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 3. 清理旧索引 ----------
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_group_leader_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP INDEX idx_group_leader_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_deputy_leader_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP INDEX idx_deputy_leader_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_department_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP INDEX idx_department_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_profession_id') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP INDEX idx_profession_id',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_inspection_time') > 0,
  'ALTER TABLE sys_monthly_inspection_task DROP INDEX idx_inspection_time',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 4. 新增时间字段索引（已存在则跳过） ----------
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_inspection_start_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD INDEX idx_inspection_start_time (inspection_start_time)',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_publicity_start_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD INDEX idx_publicity_start_time (publicity_start_time)',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'sys_monthly_inspection_task' AND INDEX_NAME = 'idx_task_name') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD INDEX idx_task_name (task_name)',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 5. 验证表结构 ----------
DESC sys_monthly_inspection_task;
