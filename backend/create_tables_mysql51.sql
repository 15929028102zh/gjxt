-- MySQL 5.1 兼容版本建表脚本
-- 注意：MySQL 5.1 不支持 utf8mb4，使用 utf8
-- 注意：MySQL 5.1 对 CURRENT_TIMESTAMP 的支持有限制

-- 部门表
CREATE TABLE `sys_dept` (
  `id` varchar(36) NOT NULL COMMENT '部门ID',
  `dept_name` varchar(100) NOT NULL COMMENT '部门名称',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父部门ID',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

-- 用户表
CREATE TABLE `sys_user` (
  `id` varchar(36) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `real_name` varchar(100) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `role` varchar(50) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员，USER-普通用户',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 专业表
CREATE TABLE `sys_profession` (
  `id` varchar(36) NOT NULL COMMENT '专业ID',
  `profession_name` varchar(100) NOT NULL COMMENT '专业名称',
  `profession_code` varchar(50) DEFAULT NULL COMMENT '专业编码',
  `description` text COMMENT '专业描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_profession_code` (`profession_code`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专业表';

-- 地点表
CREATE TABLE `sys_location` (
  `id` varchar(36) NOT NULL COMMENT '地点ID',
  `location_name` varchar(100) NOT NULL COMMENT '地点名称',
  `location_code` varchar(50) DEFAULT NULL COMMENT '地点编码',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父地点ID',
  `location_type` varchar(50) DEFAULT NULL COMMENT '地点类型',
  `description` text COMMENT '地点描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_location_code` (`location_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地点表';

-- 巡检任务表
CREATE TABLE `sys_inspection_task` (
  `id` varchar(36) NOT NULL COMMENT '任务ID',
  `task_name` varchar(200) NOT NULL COMMENT '任务名称',
  `task_type` varchar(50) NOT NULL COMMENT '任务类型',
  `inspection_date` date NOT NULL COMMENT '巡检日期',
  `group_leader_id` varchar(36) NOT NULL COMMENT '组长ID',
  `deputy_leader_id` varchar(36) DEFAULT NULL COMMENT '副组长ID',
  `department_id` varchar(36) NOT NULL COMMENT '部门ID',
  `profession_id` varchar(36) NOT NULL COMMENT '专业ID',
  `location_id` varchar(36) NOT NULL COMMENT '地点ID',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已完成',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_group_leader_id` (`group_leader_id`),
  KEY `idx_deputy_leader_id` (`deputy_leader_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_profession_id` (`profession_id`),
  KEY `idx_location_id` (`location_id`),
  KEY `idx_inspection_date` (`inspection_date`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='巡检任务表';

-- 巡检记录表
CREATE TABLE `sys_inspection_record` (
  `id` varchar(36) NOT NULL COMMENT '记录ID',
  `task_id` varchar(36) NOT NULL COMMENT '任务ID',
  `record_no` varchar(100) NOT NULL COMMENT '记录编号',
  `inspection_date` date NOT NULL COMMENT '巡检日期',
  `group_leader_id` varchar(36) NOT NULL COMMENT '组长ID',
  `deputy_leader_id` varchar(36) DEFAULT NULL COMMENT '副组长ID',
  `department_id` varchar(36) NOT NULL COMMENT '部门ID',
  `profession_id` varchar(36) NOT NULL COMMENT '专业ID',
  `location_id` varchar(36) NOT NULL COMMENT '地点ID',
  `problem_description` text NOT NULL COMMENT '问题描述',
  `problem_image_urls` text COMMENT '问题图片URL（多个URL用逗号分隔）',
  `rectification_deadline` datetime NOT NULL COMMENT '整改期限',
  `inspector` varchar(255) NOT NULL COMMENT '检查人',
  `responsible_unit` varchar(255) NOT NULL COMMENT '责任单位',
  `responsible_person` varchar(255) NOT NULL COMMENT '责任人',
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
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-待整改，1-已整改，2-已复查',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_group_leader_id` (`group_leader_id`),
  KEY `idx_deputy_leader_id` (`deputy_leader_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_profession_id` (`profession_id`),
  KEY `idx_location_id` (`location_id`),
  KEY `idx_inspector` (`inspector`),
  KEY `idx_responsible_unit` (`responsible_unit`),
  KEY `idx_responsible_person` (`responsible_person`),
  KEY `idx_reviewer_id` (`reviewer_id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='巡检记录表';

-- 插入管理员用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `department`, `position`, `role`, `status`, `create_time`, `update_time`) 
VALUES ('admin-001', 'admin', '', 'admin', '13800138000', 'admin@gjxt.com', 'IT', 'admin', 'ADMIN', 1, NOW(), NOW());