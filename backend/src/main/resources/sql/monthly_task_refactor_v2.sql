-- =============================================================
-- 月检功能重构 v2
-- 1) 月检任务：删除组长/副组长/部门/专业列，新增任务名称、月检时间段、公示时间段
-- 2) 月检记录：新增编辑待审核相关字段
-- =============================================================

-- 1) 月检任务表结构调整
ALTER TABLE `sys_monthly_inspection_task`
    ADD COLUMN `task_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '任务名称' AFTER `task_no`,
    ADD COLUMN `inspection_start_time` DATETIME DEFAULT NULL COMMENT '月检开始时间' AFTER `task_name`,
    ADD COLUMN `inspection_end_time` DATETIME DEFAULT NULL COMMENT '月检结束时间' AFTER `inspection_start_time`,
    ADD COLUMN `publicity_start_time` DATETIME DEFAULT NULL COMMENT '公示开始时间' AFTER `inspection_end_time`,
    ADD COLUMN `publicity_end_time` DATETIME DEFAULT NULL COMMENT '公示结束时间' AFTER `publicity_start_time`;

-- 删除旧的单点时间列（若未使用则可清理；保守起见仅当列存在时执行）
-- 注：原 inspection_time 字段仍保留以兼容老数据读取；新代码将不再写入
-- ALTER TABLE `sys_monthly_inspection_task` DROP COLUMN `inspection_time`;

-- 删除已废弃字段（按用户要求从表中直接删除）
ALTER TABLE `sys_monthly_inspection_task`
    DROP COLUMN `group_leader_id`,
    DROP COLUMN `deputy_leader_id`,
    DROP COLUMN `department_id`,
    DROP COLUMN `profession_id`;

-- 回填任务名称：将 task_no 同步至 task_name，避免历史数据缺失
UPDATE `sys_monthly_inspection_task`
SET `task_name` = `task_no`
WHERE `task_name` IS NULL OR `task_name` = '';

-- 2) 月检记录表：新增编辑审核相关字段
ALTER TABLE `sys_monthly_inspection_record`
    ADD COLUMN `record_edit_audit_type` TINYINT DEFAULT NULL COMMENT '待审核类型: NULL=无, 0=回退审核, 1=编辑审核' AFTER `record_status`,
    ADD COLUMN `modify_description` VARCHAR(1000) DEFAULT NULL COMMENT '修改说明（编辑审核时由申请人填写）' AFTER `record_edit_audit_type`,
    ADD COLUMN `modify_user_id` VARCHAR(64) DEFAULT NULL COMMENT '修改申请人ID' AFTER `modify_description`,
    ADD COLUMN `modify_user_name` VARCHAR(100) DEFAULT NULL COMMENT '修改申请人姓名' AFTER `modify_user_id`,
    ADD COLUMN `modify_time` DATETIME DEFAULT NULL COMMENT '修改申请时间' AFTER `modify_user_name`;
