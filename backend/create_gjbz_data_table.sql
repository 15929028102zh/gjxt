-- 创建工程标准数据表
USE `gjxt_inspection`;

CREATE TABLE `gjbz_data` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(100) DEFAULT NULL COMMENT '类型',
  `moudle` varchar(100) DEFAULT NULL COMMENT '模块',
  `responsibility` varchar(200) DEFAULT NULL COMMENT '责任单位',
  `issue` varchar(200) DEFAULT NULL COMMENT '问题类型',
  `quality` varchar(100) DEFAULT NULL COMMENT '质量等级',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_moudle` (`moudle`),
  KEY `idx_responsibility` (`responsibility`),
  KEY `idx_issue` (`issue`),
  KEY `idx_quality` (`quality`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工程标准数据表';

-- 插入一些示例数据
INSERT INTO `gjbz_data` (`type`, `moudle`, `responsibility`, `issue`, `quality`) VALUES
('安全检查', '电气系统', '电气部', '线路老化', '一般'),
('质量检查', '机械设备', '机械部', '设备磨损', '严重'),
('环保检查', '废水处理', '环保部', '排放超标', '重大'),
('消防检查', '消防设施', '安全部', '设备故障', '一般'),
('设备检查', '生产设备', '生产部', '运行异常', '严重');