-- 为sys_user表添加is_admin字段
-- 用于存储管理员权限配置

ALTER TABLE sys_user ADD COLUMN is_admin INT DEFAULT 0 COMMENT '是否为管理员（0:否, 1:是）';

-- 更新现有数据，将role为admin的用户设置为管理员
UPDATE sys_user SET is_admin = 1 WHERE role = 'admin';