-- 为周检记录表添加审核相关字段
ALTER TABLE sys_inspection_record 
ADD COLUMN  audit_status INT DEFAULT 0 COMMENT '审核状态（0:未审核, 1:已审核）',
ADD COLUMN  audit_reason VARCHAR(500) COMMENT '审核意见',
ADD COLUMN  auditor_id VARCHAR(36) COMMENT '审核人ID',
ADD COLUMN  audit_time DATETIME COMMENT '审核时间';

-- 为月检记录表添加审核相关字段
ALTER TABLE sys_monthly_inspection_record 
ADD COLUMN  audit_status INT DEFAULT 0 COMMENT '审核状态（0:未审核, 1:已审核）',
ADD COLUMN  audit_reason VARCHAR(500) COMMENT '审核意见',
ADD COLUMN  auditor_id VARCHAR(36) COMMENT '审核人ID',
ADD COLUMN  audit_time DATETIME COMMENT '审核时间';

-- 更新状态值说明
-- 周检记录状态：0-待整改, 1-待复查, 2-已完成, 3-待审核
-- 月检记录状态：0-草稿, 1-已提交, 2-已审核, 3-待审核
