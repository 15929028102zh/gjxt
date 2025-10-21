-- 月检记录表建表语句
USE `gjxt_inspection`;

-- 月检记录表
CREATE TABLE `sys_monthly_inspection_record` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `record_no` varchar(50) NOT NULL COMMENT '记录编号',
  `task_id` varchar(36) NOT NULL COMMENT '关联的月检任务ID',
  
  -- 级联选择字段
  `professional_type_id` varchar(36) DEFAULT NULL COMMENT '专业类别ID',
  `professional_type_name` varchar(100) DEFAULT NULL COMMENT '专业类别名称',
  `module_id` varchar(36) DEFAULT NULL COMMENT '模块ID',
  `module_name` varchar(100) DEFAULT NULL COMMENT '模块名称',
  `responsibility_id` varchar(36) DEFAULT NULL COMMENT '责任部门ID',
  `responsibility_name` varchar(100) DEFAULT NULL COMMENT '责任部门名称',
  `issue_id` varchar(36) DEFAULT NULL COMMENT '问题ID',
  `issue_name` varchar(200) DEFAULT NULL COMMENT '问题名称',
  `quality_id` varchar(36) DEFAULT NULL COMMENT '性质界定ID',
  `quality_name` varchar(100) DEFAULT NULL COMMENT '性质界定名称',
  
  -- 表单录入字段
  `problem_type` varchar(50) DEFAULT NULL COMMENT '问题类型',
  `problem_description` text DEFAULT NULL COMMENT '问题描述',
  `location` varchar(200) DEFAULT NULL COMMENT '发现位置',
  `suggestion` text DEFAULT NULL COMMENT '整改建议',
  `photos` text DEFAULT NULL COMMENT '问题照片URL列表（JSON格式）',
  `safety_score` decimal(5,2) DEFAULT NULL COMMENT '安全记分',
  `rectification_date` date DEFAULT NULL COMMENT '整改期限',
  `inspector` varchar(100) DEFAULT NULL COMMENT '检查人',
  
  -- 系统字段
  `record_status` tinyint NOT NULL DEFAULT '0' COMMENT '记录状态：0-草稿，1-已提交，2-已审核',
  `create_user_id` varchar(36) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_professional_type_id` (`professional_type_id`),
  KEY `idx_module_id` (`module_id`),
  KEY `idx_responsibility_id` (`responsibility_id`),
  KEY `idx_issue_id` (`issue_id`),
  KEY `idx_quality_id` (`quality_id`),
  KEY `idx_record_status` (`record_status`),
  KEY `idx_create_user_id` (`create_user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月检记录表';

-- 添加外键约束（可选，根据实际需要）
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_task` FOREIGN KEY (`task_id`) REFERENCES `sys_monthly_inspection_task` (`id`);
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_create_user` FOREIGN KEY (`create_user_id`) REFERENCES `sys_user` (`id`);
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_professional_type` FOREIGN KEY (`professional_type_id`) REFERENCES `sys_assessment_dict` (`id`);
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_module` FOREIGN KEY (`module_id`) REFERENCES `sys_assessment_dict` (`id`);
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_responsibility` FOREIGN KEY (`responsibility_id`) REFERENCES `sys_assessment_dict` (`id`);
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_issue` FOREIGN KEY (`issue_id`) REFERENCES `sys_assessment_dict` (`id`);
-- ALTER TABLE `sys_monthly_inspection_record` ADD CONSTRAINT `fk_record_quality` FOREIGN KEY (`quality_id`) REFERENCES `sys_assessment_dict` (`id`);

-- 创建记录编号生成函数（可选）
-- DELIMITER //
-- CREATE FUNCTION generate_record_no() RETURNS VARCHAR(50)
-- READS SQL DATA
-- DETERMINISTIC
-- BEGIN
--     DECLARE record_count INT DEFAULT 0;
--     DECLARE new_record_no VARCHAR(50);
--     
--     SELECT COUNT(*) INTO record_count FROM sys_monthly_inspection_record WHERE DATE(create_time) = CURDATE();
--     SET new_record_no = CONCAT('YJR', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(record_count + 1, 4, '0'));
--     
--     RETURN new_record_no;
-- END //
-- DELIMITER ;