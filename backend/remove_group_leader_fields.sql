-- 删除检查记录表中的检查组长和副组长字段
USE gjxt_inspection;

-- 删除 group_leader_id 字段
ALTER TABLE sys_inspection_record DROP COLUMN group_leader_id;

-- 删除 deputy_leader_id 字段
ALTER TABLE sys_inspection_record DROP COLUMN deputy_leader_id;

-- 验证字段删除
DESC sys_inspection_record;