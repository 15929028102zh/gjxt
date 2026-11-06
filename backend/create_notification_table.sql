-- 创建通知表
USE `gjxt_inspection`;

CREATE TABLE IF NOT EXISTS `notification` (
  `id` varchar(36) NOT NULL COMMENT '通知ID',
  `type` varchar(50) NOT NULL COMMENT '通知类型',
  `title` varchar(200) NOT NULL COMMENT '通知标题',
  `content` text NOT NULL COMMENT '通知内容',
  `user_id` varchar(36) NOT NULL COMMENT '接收用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '接收用户名',
  `task_id` varchar(36) DEFAULT NULL COMMENT '任务ID（如果是任务相关通知）',
  `task_no` varchar(50) DEFAULT NULL COMMENT '任务编号',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读（0:未读, 1:已读）',
  `create_time` datetime NOT NULL  COMMENT '创建时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `priority` varchar(20) DEFAULT 'medium' COMMENT '优先级（high, medium, low）',
  `action_url` varchar(500) DEFAULT NULL COMMENT '操作链接',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- 插入一些测试数据
INSERT INTO `notification` (`id`, `type`, `title`, `content`, `user_id`, `user_name`, `task_id`, `task_no`, `is_read`, `create_time`, `expire_time`, `priority`, `action_url`) VALUES
('test-notification-1', 'task', '新任务分配', '您有一个新的巡检任务需要处理', '1', 'admin', 'task-001', 'T20241029001', 0, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'high', '/task/detail/task-001'),
('test-notification-2', 'system', '系统维护通知', '系统将于今晚进行维护，请提前保存工作', '1', 'admin', NULL, NULL, 0, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'medium', NULL),
('test-notification-3', 'reminder', '任务提醒', '您的任务即将到期，请及时处理', '1', 'admin', 'task-002', 'T20241029002', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 3 DAY), 'high', '/task/detail/task-002');