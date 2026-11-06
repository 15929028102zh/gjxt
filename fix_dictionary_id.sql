-- 修改sys_dictionary表的id字段类型
USE gjxt_inspection;

-- 删除外键约束（如果存在）
-- ALTER TABLE sys_dictionary DROP FOREIGN KEY IF EXISTS fk_dictionary_id;

-- 删除主键约束
ALTER TABLE sys_dictionary DROP PRIMARY KEY;

-- 修改id字段类型为varchar(36)
ALTER TABLE sys_dictionary MODIFY COLUMN id VARCHAR(36) NOT NULL;

-- 重新添加主键约束
ALTER TABLE sys_dictionary ADD PRIMARY KEY (id);

-- 显示修改后的表结构
DESCRIBE sys_dictionary;