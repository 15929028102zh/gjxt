-- 修改sys_user表的ID字段类型以支持UUID
USE `gjxt_inspection`;

-- 首先删除自增属性
ALTER TABLE `sys_user` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID';

-- 然后修改字段类型为varchar(36)
ALTER TABLE `sys_user` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';

-- 如果有外键引用，也需要修改相关表的字段类型
-- 检查sys_monthly_inspection_task表是否存在相关字段
-- ALTER TABLE `sys_monthly_inspection_task` MODIFY COLUMN `group_leader_id` varchar(36) DEFAULT NULL COMMENT '组长ID';
-- ALTER TABLE `sys_monthly_inspection_task` MODIFY COLUMN `deputy_leader_id` varchar(36) DEFAULT NULL COMMENT '副组长ID';
-- ALTER TABLE `sys_monthly_inspection_task` MODIFY COLUMN `create_user_id` varchar(36) NOT NULL COMMENT '创建人ID';

-- 检查sys_inspection_record表是否存在相关字段
-- ALTER TABLE `sys_inspection_record` MODIFY COLUMN `reviewer_id` varchar(36) DEFAULT NULL COMMENT '复查人ID';

-- 注意：执行此脚本前请备份数据库
-- 执行后需要清空现有数据或手动更新现有记录的ID值为UUID格式