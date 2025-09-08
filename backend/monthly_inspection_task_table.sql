-- 月检任务表建表语句
USE `gjxt_inspection`;

-- 月检任务表
CREATE TABLE `sys_monthly_inspection_task` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `task_no` varchar(50) NOT NULL COMMENT '任务编号',
  `inspection_time` varchar(20) NOT NULL COMMENT '月检时间（格式：YYYY-MM-DD HH）',
  `group_leader_id` varchar(36) NOT NULL COMMENT '月检组长ID',
  `deputy_leader_id` varchar(36) DEFAULT NULL COMMENT '月检副组长ID',
  `department_id` varchar(36) NOT NULL COMMENT '部门ID',
  `profession_id` varchar(36) NOT NULL COMMENT '专业ID',
  `task_description` text COMMENT '任务描述',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已完成',
  `notification_sent` tinyint NOT NULL DEFAULT '0' COMMENT '通知发送状态：0-未发送，1-已发送',
  `create_user_id` varchar(36) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_no` (`task_no`),
  KEY `idx_group_leader_id` (`group_leader_id`),
  KEY `idx_deputy_leader_id` (`deputy_leader_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_profession_id` (`profession_id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_inspection_time` (`inspection_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月检任务表';

-- 添加外键约束（可选，根据实际需要）
-- ALTER TABLE `sys_monthly_inspection_task` ADD CONSTRAINT `fk_monthly_task_group_leader` FOREIGN KEY (`group_leader_id`) REFERENCES `sys_user` (`id`);
-- ALTER TABLE `sys_monthly_inspection_task` ADD CONSTRAINT `fk_monthly_task_deputy_leader` FOREIGN KEY (`deputy_leader_id`) REFERENCES `sys_user` (`id`);
-- ALTER TABLE `sys_monthly_inspection_task` ADD CONSTRAINT `fk_monthly_task_department` FOREIGN KEY (`department_id`) REFERENCES `sys_dept` (`id`);
-- ALTER TABLE `sys_monthly_inspection_task` ADD CONSTRAINT `fk_monthly_task_profession` FOREIGN KEY (`profession_id`) REFERENCES `sys_profession` (`id`);
-- ALTER TABLE `sys_monthly_inspection_task` ADD CONSTRAINT `fk_monthly_task_create_user` FOREIGN KEY (`create_user_id`) REFERENCES `sys_user` (`id`);