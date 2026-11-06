-- 修改用户表，添加部门ID字段
USE gjxt_inspection;

-- 添加部门ID字段
ALTER TABLE sys_user ADD COLUMN dept_id BIGINT COMMENT '部门ID';

-- 添加外键约束
ALTER TABLE sys_user ADD CONSTRAINT fk_user_dept FOREIGN KEY (dept_id) REFERENCES sys_dept(id);

-- 可选：如果需要删除原来的department字段，取消下面的注释
-- ALTER TABLE sys_user DROP COLUMN department;