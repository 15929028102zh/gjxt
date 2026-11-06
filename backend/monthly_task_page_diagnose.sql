-- ============================================================
-- 月检任务表分页慢诊断 SQL
-- 适用：执行前先确认当前索引情况，再用 EXPLAIN 查看执行计划
-- 用法：在生产 MySQL 上直接执行（仅 SELECT，安全）
-- ============================================================

USE gjxt_inspection;

-- ---------- 1. 查看现有索引 ----------
SELECT
  INDEX_NAME,
  GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX) AS columns,
  COUNT(*) AS col_count
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'sys_monthly_inspection_task'
GROUP BY INDEX_NAME
ORDER BY INDEX_NAME;

-- ---------- 2. 表数据量 ----------
SELECT
  TABLE_NAME,
  TABLE_ROWS,
  ROUND(DATA_LENGTH / 1024 / 1024, 2) AS data_mb,
  ROUND(INDEX_LENGTH / 1024 / 1024, 2) AS index_mb
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'sys_monthly_inspection_task';

-- ---------- 3. EXPLAIN 默认场景：仅 deleted 过滤 + create_time 排序 ----------
EXPLAIN
SELECT t.id, t.task_no, t.task_name
FROM sys_monthly_inspection_task t
WHERE t.deleted = 0
ORDER BY t.create_time DESC
LIMIT 10;

-- ---------- 4. EXPLAIN 带 status 过滤 ----------
EXPLAIN
SELECT t.id, t.task_no, t.task_name
FROM sys_monthly_inspection_task t
WHERE t.deleted = 0 AND t.status = 1
ORDER BY t.create_time DESC
LIMIT 10;

-- ---------- 5. EXPLAIN 带时间段过滤 ----------
EXPLAIN
SELECT t.id, t.task_no, t.task_name
FROM sys_monthly_inspection_task t
WHERE t.deleted = 0
  AND t.inspection_start_time >= '2026-01-01'
  AND t.inspection_end_time <= '2026-12-31 23:59:59'
ORDER BY t.create_time DESC
LIMIT 10;

-- ---------- 6. EXPLAIN COUNT(*) 默认 ----------
EXPLAIN
SELECT COUNT(*)
FROM sys_monthly_inspection_task
WHERE deleted = 0;

-- ---------- 7. 查看是否有未走索引的全表扫描 ----------
SELECT
  DIGEST_TEXT,
  COUNT_STAR AS exec_count,
  ROUND(AVG_TIMER_WAIT / 1000000000, 2) AS avg_ms,
  ROUND(SUM_TIMER_WAIT / 1000000000, 2) AS sum_ms
FROM performance_schema.events_statements_summary_by_digest
WHERE SCHEMA_NAME = DATABASE()
  AND DIGEST_TEXT LIKE '%sys_monthly_inspection_task%'
ORDER BY avg_ms DESC
LIMIT 20;
