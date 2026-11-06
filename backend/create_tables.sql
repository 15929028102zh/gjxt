-- 创建数据库表结构
USE `gjxt_inspection`;

-- 部门表
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dept_name` varchar(100) NOT NULL COMMENT '部门名称',
  `description` text COMMENT '部门描述',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父部门ID',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 用户表
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员，USER-普通用户',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 专业表
CREATE TABLE `sys_profession` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `profession_name` varchar(100) NOT NULL COMMENT '专业名称',
  `description` text COMMENT '专业描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专业表';

-- 部位表
CREATE TABLE `sys_location` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `location_name` varchar(100) NOT NULL COMMENT '部位名称',
  `description` text COMMENT '部位描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部位表';



-- 巡检任务表
CREATE TABLE `sys_inspection_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `task_name` varchar(200) NOT NULL COMMENT '任务名称',
  `description` text COMMENT '任务描述',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`),
  KEY `idx_date_range` (`start_date`, `end_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='巡检任务表';

-- 巡检记录表
CREATE  TABLE `sys_inspection_record` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `record_no` varchar(50) NOT NULL COMMENT '记录编号',
  `group_leader_id` varchar(36) COMMENT '检查组组长ID',
  `deputy_leader_id` varchar(36) COMMENT '检查组副组长ID',
  `department_id` varchar(36) NOT NULL COMMENT '所属部门ID',
  `profession_id` varchar(36) NOT NULL COMMENT '专业ID',
  `problem_description` text NOT NULL COMMENT '问题描述',
  `location` varchar(255) NOT NULL COMMENT '发生部位',--录入文本
  `problem_image_urls` text COMMENT '问题图片URL（多个URL用逗号分隔）',
  `rectification_deadline` datetime NOT NULL COMMENT '整改期限',
  `inspector` varchar(255) NOT NULL COMMENT '检查人',--录入不需要字典
  `responsible_unit` varchar(255) NOT NULL COMMENT '责任单位',--录入 无需字典
  `responsible_person` varchar(255) NOT NULL COMMENT '责任人',--
  `rectification_effect` text COMMENT '整改效果',
  `rectification_image_urls` text COMMENT '整改后图片URL（多个URL用逗号分隔）',
  `rectification_time` datetime DEFAULT NULL COMMENT '整改时间',
  `review_effect` text COMMENT '复查效果',
  `reviewer_id` varchar(36) DEFAULT NULL COMMENT '复查人ID',
  `review_time` datetime DEFAULT NULL COMMENT '复查时间',
  `review_image_urls` text COMMENT '复查照片URL（多个URL用逗号分隔）',
  `nature_definition` varchar(100) COMMENT '性质界定',
  `assessment_situation` text COMMENT '考核情况',
  `safety_score` decimal(10,2) DEFAULT NULL COMMENT '安全记分',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待整改，1-已整改，2-已复查',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_group_leader_id` (`group_leader_id`),
  KEY `idx_deputy_leader_id` (`deputy_leader_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_profession_id` (`profession_id`),
  KEY `idx_inspector` (`inspector`),
  KEY `idx_responsible_unit` (`responsible_unit`),
  KEY `idx_responsible_person` (`responsible_person`),
  KEY `idx_reviewer_id` (`reviewer_id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='周检记录表';

-- 插入管理员用户
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `department`, `position`, `role`, `status`) 
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin', '13800138000', 'admin@gjxt.com', 'IT', 'admin', 'ADMIN', 1);