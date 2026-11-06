-- 为通知表添加部门字段，支持按部门过滤推送
USE `gjxt_inspection`;

-- 添加部门相关字段
ALTER TABLE `notification` 
ADD COLUMN `department_id` varchar(36) DEFAULT NULL COMMENT '部门ID（用于按部门过滤推送）' AFTER `user_name`,
ADD COLUMN `department_name` varchar(100) DEFAULT NULL COMMENT '部门名称' AFTER `department_id`;

-- 添加部门相关索引
ALTER TABLE `notification` 
ADD KEY `idx_department_id` (`department_id`);

-- 更新现有数据的部门信息（从用户表关联获取）
UPDATE `notification` n 
INNER JOIN `sys_user` u ON n.user_id = u.id 
INNER JOIN `sys_dept` d ON u.dept_id = d.id 
SET n.department_id = d.id, n.department_name = d.dept_name 
WHERE n.department_id IS NULL;