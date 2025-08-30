-- 修复字典表ID字段类型
USE gjxt_inspection;

-- 删除自增属性并修改ID字段类型为varchar
ALTER TABLE sys_dictionary MODIFY COLUMN id varchar(36) NOT NULL;

-- 删除主键约束
ALTER TABLE sys_dictionary DROP PRIMARY KEY;

-- 重新添加主键约束
ALTER TABLE sys_dictionary ADD PRIMARY KEY (id);

-- 查看修改后的表结构
DESCRIBE sys_dictionary;