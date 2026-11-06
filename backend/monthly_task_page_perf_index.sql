-- ============================================================
-- 月检任务表 sys_monthly_inspection_task 分页查询性能优化
-- 适用：GET /api/monthly-inspection-task/page 首次打开慢
--
-- 慢的原因分析：
--   当前 `selectTaskPage` SQL：
--     SELECT ... FROM sys_monthly_inspection_task t
--     LEFT JOIN sys_user cu ON t.create_user_id = cu.id ...
--     WHERE t.deleted = 0
--     [AND t.status = ?]
--     [AND (t.task_name LIKE '%xxx%' OR t.task_no LIKE '%xxx%')]
--     [AND t.inspection_start_time >= ?]
--     [AND t.inspection_end_time <= ?]
--     ORDER BY t.create_time DESC
--     LIMIT ?
--
--   慢点 1：MyBatis Plus 的 PaginationInnerInterceptor 在分页前
--           会先执行一次 SELECT COUNT(*)，随后再执行带 LIMIT 的 SELECT。
--           单独的 idx_deleted + idx_create_time 各自只能帮 COUNT 或 ORDER BY 之一，
--           MySQL 实际执行顺序在表数据量上来后会出现 filesort / 全表扫描。
--   慢点 2：list 为 IN (SELECT id FROM (原 SQL) t) 包装写法时，部分版本不会触发 sort 优化。
--   慢点 3：idx_inspection_start_time 是单列索引，遇到
--           `inspection_end_time <= ?` 这类过滤需要回表后再过滤。
--
-- 优化方案：新增以下复合索引
-- ============================================================

USE gjxt_inspection;

-- ---------- 1. 复合索引 (deleted, create_time) ----------
-- 覆盖：默认场景 WHERE deleted=0 ORDER BY create_time DESC
--       即最常见的"打开月检任务列表"无任何过滤的请求
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_monthly_inspection_task'
      AND INDEX_NAME = 'idx_deleted_create_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD INDEX idx_deleted_create_time (deleted, create_time)',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 2. 复合索引 (deleted, status, create_time) ----------
-- 覆盖：带状态过滤的分页（任务管理界面常见的 status 筛选）
-- 同时仍然可用于：WHERE deleted=0 ORDER BY create_time（最左前缀规则）
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_monthly_inspection_task'
      AND INDEX_NAME = 'idx_deleted_status_create_time') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD INDEX idx_deleted_status_create_time (deleted, status, create_time)',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 3. 复合索引 (inspection_start_time, inspection_end_time) ----------
-- 覆盖：按月检时间段筛选（管理员按月份搜索当月任务时使用）
-- 单列 idx_inspection_start_time 无法同时利用 end_time 范围
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_monthly_inspection_task'
      AND INDEX_NAME = 'idx_inspection_time_range') = 0,
  'ALTER TABLE sys_monthly_inspection_task ADD INDEX idx_inspection_time_range (inspection_start_time, inspection_end_time)',
  'SELECT 1'));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ---------- 4. 验证索引已生效 ----------
SELECT INDEX_NAME, COLUMN_NAME, SEQ_IN_INDEX
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'sys_monthly_inspection_task'
  AND INDEX_NAME IN (
    'idx_deleted_create_time',
    'idx_deleted_status_create_time',
    'idx_inspection_time_range'
  )
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- ---------- 5. 验证表行数与索引体积 ----------
SELECT
  TABLE_NAME,
  TABLE_ROWS,
  ROUND(DATA_LENGTH / 1024 / 1024, 2) AS data_mb,
  ROUND(INDEX_LENGTH / 1024 / 1024, 2) AS index_mb
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'sys_monthly_inspection_task';
