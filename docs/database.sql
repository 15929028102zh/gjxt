-- 石油工业园区设备巡检系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `gjxt_inspection` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `gjxt_inspection`;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增长',
  `username` varchar(50) NOT NULL COMMENT '用户名，用于登录',
  `password` varchar(100) NOT NULL COMMENT '用户密码，加密存储',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `department` varchar(50) DEFAULT NULL COMMENT '所属部门',
  `role` varchar(20) NOT NULL DEFAULT 'inspector' COMMENT '用户角色（inspector:巡检员, reviewer:复查员, admin:管理员）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '用户状态（0:禁用, 1:启用）',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0:未删除, 1:已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 专业表
DROP TABLE IF EXISTS `sys_profession`;
CREATE TABLE `sys_profession` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '专业ID，主键，自增长',
  `profession_name` varchar(50) NOT NULL COMMENT '专业名称（如电气、机械等）',
  `profession_code` varchar(20) DEFAULT NULL COMMENT '专业编码',
  `description` varchar(200) DEFAULT NULL COMMENT '专业描述',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0:禁用, 1:启用）',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0:未删除, 1:已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_profession_name` (`profession_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专业表';

-- 发生部位表
DROP TABLE IF EXISTS `sys_location`;
CREATE TABLE `sys_location` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部位ID，主键，自增长',
  `location_name` varchar(50) NOT NULL COMMENT '部位名称（如压缩机、管道等）',
  `location_code` varchar(20) DEFAULT NULL COMMENT '部位编码',
  `description` varchar(200) DEFAULT NULL COMMENT '部位描述',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0:禁用, 1:启用）',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0:未删除, 1:已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_location_name` (`location_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发生部位表';

-- 扣分性质表
DROP TABLE IF EXISTS `sys_deduction_type`;
CREATE TABLE `sys_deduction_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '扣分性质ID，主键，自增长',
  `deduction_type_name` varchar(50) NOT NULL COMMENT '扣分性质名称（如严重违规、一般问题等）',
  `score` int NOT NULL COMMENT '对应的扣分分数',
  `description` varchar(200) DEFAULT NULL COMMENT '扣分性质描述',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0:禁用, 1:启用）',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0:未删除, 1:已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_deduction_type_name` (`deduction_type_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='扣分性质表';

-- 巡检任务表
DROP TABLE IF EXISTS `sys_inspection_task`;
CREATE TABLE `sys_inspection_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID，主键，自增长',
  `task_no` varchar(50) NOT NULL COMMENT '任务编号',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `profession_id` bigint NOT NULL COMMENT '所属专业ID，外键，关联sys_profession表',
  `location_id` bigint NOT NULL COMMENT '发生部位ID，外键，关联sys_location表',
  `user_id` bigint NOT NULL COMMENT '接收任务用户ID，外键，关联sys_user表',
  `schedule_time` datetime NOT NULL COMMENT '计划巡检时间',
  `rectification_deadline` datetime NOT NULL COMMENT '最后整改时间',
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '任务状态（pending:未开始, in_progress:进行中, completed:已完成, cancelled:已取消）',
  `description` text COMMENT '任务描述',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0:未删除, 1:已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '任务更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_no` (`task_no`),
  KEY `idx_profession_id` (`profession_id`),
  KEY `idx_location_id` (`location_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_time` (`schedule_time`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_task_profession` FOREIGN KEY (`profession_id`) REFERENCES `sys_profession` (`id`),
  CONSTRAINT `fk_task_location` FOREIGN KEY (`location_id`) REFERENCES `sys_location` (`id`),
  CONSTRAINT `fk_task_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='巡检任务表';

-- 巡检记录表
DROP TABLE IF EXISTS `sys_inspection_record`;
CREATE TABLE `sys_inspection_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID，主键，自增长',
  `record_no` varchar(50) NOT NULL COMMENT '记录编号',
  `task_id` bigint NOT NULL COMMENT '所属巡检任务ID，外键，关联sys_inspection_task表',
  `profession_id` bigint NOT NULL COMMENT '问题所属专业ID，外键，关联sys_profession表',
  `location_id` bigint NOT NULL COMMENT '问题发生部位ID，外键，关联sys_location表',
  `checker_id` bigint NOT NULL COMMENT '检查人ID，外键，关联sys_user表',
  `responsible_person` varchar(50) NOT NULL COMMENT '责任人',
  `deduction_type_id` bigint NOT NULL COMMENT '扣分性质ID，外键，关联sys_deduction_type表',
  `safety_score` int DEFAULT '0' COMMENT '安全积分',
  `problem_description` text COMMENT '问题描述',
  `problem_photo` varchar(500) COMMENT '问题现场照片路径（多张照片用逗号分隔）',
  `rectification_deadline` datetime NOT NULL COMMENT '最后整改时间',
  `rectification_description` text COMMENT '整改说明',
  `rectification_photo` varchar(500) COMMENT '整改照片路径（多张照片用逗号分隔）',
  `reviewer_id` bigint DEFAULT NULL COMMENT '复查人ID，外键，关联sys_user表',
  `review_time` datetime DEFAULT NULL COMMENT '复查时间',
  `review_result` varchar(20) DEFAULT NULL COMMENT '复查结果（pass:通过, reject:不通过）',
  `review_remark` text COMMENT '复查备注',
  `status` varchar(20) NOT NULL DEFAULT 'pending_rectification' COMMENT '问题状态（pending_rectification:待整改, pending_review:待确认, completed:已整改, overdue:延迟整改）',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0:未删除, 1:已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_profession_id` (`profession_id`),
  KEY `idx_location_id` (`location_id`),
  KEY `idx_checker_id` (`checker_id`),
  KEY `idx_reviewer_id` (`reviewer_id`),
  KEY `idx_deduction_type_id` (`deduction_type_id`),
  KEY `idx_status` (`status`),
  KEY `idx_rectification_deadline` (`rectification_deadline`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_record_task` FOREIGN KEY (`task_id`) REFERENCES `sys_inspection_task` (`id`),
  CONSTRAINT `fk_record_profession` FOREIGN KEY (`profession_id`) REFERENCES `sys_profession` (`id`),
  CONSTRAINT `fk_record_location` FOREIGN KEY (`location_id`) REFERENCES `sys_location` (`id`),
  CONSTRAINT `fk_record_checker` FOREIGN KEY (`checker_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `fk_record_reviewer` FOREIGN KEY (`reviewer_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `fk_record_deduction_type` FOREIGN KEY (`deduction_type_id`) REFERENCES `sys_deduction_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='巡检记录表';

-- 插入初始数据

-- 插入管理员用户
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `department`, `role`, `status`) VALUES
('admin', '$2a$10$7JB720yubVSOfvVWbazBuOWWpRkQY/l/1cKw.sv8CoGv6NoqXcfS6', '系统管理员', '信息技术部', 'admin', 1),
('inspector1', '$2a$10$7JB720yubVSOfvVWbazBuOWWpRkQY/l/1cKw.sv8CoGv6NoqXcfS6', '张三', '设备维护部', 'inspector', 1),
('reviewer1', '$2a$10$7JB720yubVSOfvVWbazBuOWWpRkQY/l/1cKw.sv8CoGv6NoqXcfS6', '李四', '质量安全部', 'reviewer', 1);

-- 插入专业数据
INSERT INTO `sys_profession` (`profession_name`, `profession_code`, `description`, `sort_order`) VALUES
('电气', 'DQ', '电气设备专业', 1),
('机械', 'JX', '机械设备专业', 2),
('仪表', 'YB', '仪表设备专业', 3),
('工艺', 'GY', '工艺设备专业', 4),
('安全', 'AQ', '安全设备专业', 5);

-- 插入发生部位数据
INSERT INTO `sys_location` (`location_name`, `location_code`, `description`, `sort_order`) VALUES
('压缩机', 'YSJ', '压缩机设备', 1),
('管道', 'GD', '管道系统', 2),
('阀门', 'FM', '阀门设备', 3),
('泵', 'B', '泵类设备', 4),
('储罐', 'CG', '储罐设备', 5),
('换热器', 'HRQ', '换热器设备', 6),
('电机', 'DJ', '电机设备', 7),
('控制柜', 'KZG', '控制柜设备', 8);

-- 插入扣分性质数据
INSERT INTO `sys_deduction_type` (`deduction_type_name`, `score`, `description`, `sort_order`) VALUES
('严重违规', 10, '严重安全违规行为', 1),
('一般违规', 5, '一般安全违规行为', 2),
('轻微问题', 2, '轻微安全问题', 3),
('隐患问题', 3, '安全隐患问题', 4),
('设备故障', 8, '设备故障问题', 5);

-- 创建索引



CREATE INDEX idx_user_role ON sys_user(role);
CREATE INDEX idx_user_department ON sys_user(department);
CREATE INDEX idx_profession_status ON sys_profession(status);
CREATE INDEX idx_location_status ON sys_location(status);
CREATE INDEX idx_deduction_type_status ON sys_deduction_type(status);