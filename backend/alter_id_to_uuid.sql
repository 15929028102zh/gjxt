-- 修改所有表的ID字段类型为VARCHAR(36)以支持UUID
USE `gjxt_inspection`;

-- 1. 修改部门表
ALTER TABLE `sys_dept` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';
ALTER TABLE `sys_dept` MODIFY COLUMN `parent_id` varchar(36) NOT NULL DEFAULT '0' COMMENT '父部门ID';

-- 2. 修改用户表
ALTER TABLE `sys_user` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';

-- 3. 修改专业表
ALTER TABLE `sys_profession` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';

-- 4. 修改部位表
ALTER TABLE `sys_location` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';

-- 5. 修改扣分类型表
ALTER TABLE `sys_deduction_type` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';

-- 6. 修改巡检任务表
ALTER TABLE `sys_inspection_task` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';

-- 7. 修改巡检记录表
ALTER TABLE `sys_inspection_record` MODIFY COLUMN `id` varchar(36) NOT NULL COMMENT '主键ID';
ALTER TABLE `sys_inspection_record` MODIFY COLUMN `task_id` varchar(36) NOT NULL COMMENT '任务ID';
ALTER TABLE `sys_inspection_record` MODIFY COLUMN `inspector_id` varchar(36) NOT NULL COMMENT '巡检员ID';
ALTER TABLE `sys_inspection_record` MODIFY COLUMN `responsible_person_id` varchar(36) DEFAULT NULL COMMENT '责任人ID';

-- 注意：执行此脚本前请备份数据库
-- 执行后需要清空现有数据或手动更新现有记录的ID值为UUID格式