-- =============================================================
-- 为周检/月检记录表新增「整改人 / 复查人」姓名列
-- 需求：确认整改页展示只读「整改人」、确认复查页展示只读「复查人」，
--       取值为当前登录用户，点击确认后写入库中。
-- 环境：MySQL 5.1，utf8。列为可空增量列，不影响历史数据。
-- =============================================================

ALTER TABLE `sys_inspection_record`
  ADD COLUMN `rectifier_name` VARCHAR(100) NULL COMMENT '整改人姓名';

ALTER TABLE `sys_inspection_record`
  ADD COLUMN `reviewer_name` VARCHAR(100) NULL COMMENT '复查人姓名';

ALTER TABLE `sys_monthly_inspection_record`
  ADD COLUMN `rectifier_name` VARCHAR(100) NULL COMMENT '整改人姓名';

ALTER TABLE `sys_monthly_inspection_record`
  ADD COLUMN `reviewer_name` VARCHAR(100) NULL COMMENT '复查人姓名';
