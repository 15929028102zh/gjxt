-- 为月检记录表新增整改/复查相关字段
USE `gjxt_inspection`;

ALTER TABLE `sys_monthly_inspection_record`
  ADD COLUMN `rectification_suggestion` VARCHAR(500) NULL COMMENT '整改建议' AFTER `rectification_date`,
  ADD COLUMN `rectification_effect` VARCHAR(500) NULL COMMENT '整改效果描述' AFTER `rectification_suggestion`,
  ADD COLUMN `rectification_images` TEXT NULL COMMENT '整改图片URL列表' AFTER `rectification_effect`,
  ADD COLUMN `rectification_time` DATETIME NULL COMMENT '整改时间' AFTER `rectification_images`,
  ADD COLUMN `review_suggestion` VARCHAR(500) NULL COMMENT '复查建议' AFTER `rectification_time`,
  ADD COLUMN `review_effect` VARCHAR(500) NULL COMMENT '复查效果描述' AFTER `review_suggestion`,
  ADD COLUMN `review_images` TEXT NULL COMMENT '复查图片URL列表' AFTER `review_effect`,
  ADD COLUMN `review_time` DATETIME NULL COMMENT '复查时间' AFTER `review_images`;

-- 回滚示例（如需）
-- ALTER TABLE `sys_monthly_inspection_record`
--   DROP COLUMN `rectification_suggestion`,
--   DROP COLUMN `rectification_effect`,
--   DROP COLUMN `rectification_images`,
--   DROP COLUMN `rectification_time`,
--   DROP COLUMN `review_suggestion`,
--   DROP COLUMN `review_effect`,
--   DROP COLUMN `review_images`,
--   DROP COLUMN `review_time`;