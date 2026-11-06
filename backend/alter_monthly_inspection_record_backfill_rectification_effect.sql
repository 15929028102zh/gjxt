-- 月检记录表回填 rectification_effect 列（可重复执行）
-- 背景：
--   旧前端 RectificationConfirmDialog.vue 把"整改效果"textarea 误绑到 rectificationSuggestion，
--   导致用户实际输入的"整改效果"文字被存进了 rectification_suggestion 列，rectification_effect 永远 NULL。
--   本脚本仅在 rectification_effect IS NULL 且 rectification_suggestion 非空时把后者拷给前者，
--   与用户原意一致（用户当时只看到"整改效果"一个输入框，内容就是整改效果）。
-- 安全的（idempotent）：WHERE 条件保证只补 NULL 行，重复执行不会覆盖已有数据。
USE `gjxt_inspection`;

UPDATE `sys_monthly_inspection_record`
SET
  `rectification_effect` = `rectification_suggestion`,
  `update_time` = NOW()
WHERE `deleted` = 0
  AND `rectification_effect` IS NULL
  AND `rectification_suggestion` IS NOT NULL
  AND TRIM(`rectification_suggestion`) <> '';

-- 回滚示例（如需把刚补的数据退回 suggestion 列）：
-- UPDATE `sys_monthly_inspection_record`
-- SET `rectification_effect` = NULL
-- WHERE `deleted` = 0
--   AND `rectification_effect` = `rectification_suggestion`;