# 考核情况字典数据配置说明

## 问题描述
页面新增周检记录时考核情况字典没有正确加载，需要在数据库中插入考核情况字典数据。

## 解决方案

### 1. 执行SQL脚本
请使用MySQL客户端执行以下SQL脚本来插入考核情况字典数据：

```bash
# 方式1：直接执行SQL文件
mysql -u root -p gjxt_inspection < insert_assessment_dict.sql

# 方式2：登录MySQL后执行
mysql -u root -p
use gjxt_inspection;
source insert_assessment_dict.sql;
```

### 2. 验证数据是否插入成功
执行以下SQL查询验证：

```sql
SELECT * FROM sys_dictionary WHERE dict_type = '考核情况';
```

应该能看到以下数据：
- 合格
- 不合格  
- 优秀
- 良好
- 待评定

### 3. 重启后端服务
插入数据后，建议重启后端服务以确保字典缓存更新。

## 已完成的修改

### 1. 导出Excel功能
已在 `SysInspectionRecordServiceImpl.java` 中添加了考核情况字段：
- 在Excel标题行中添加了"考核情况"列
- 在数据行中添加了考核情况数据的输出

### 2. 字典数据
创建了 `insert_assessment_dict.sql` 脚本，包含完整的考核情况字典数据。

## 注意事项
1. 确保数据库连接正常
2. 确保有足够的权限执行INSERT操作
3. 如果字典数据已存在，可能会出现主键冲突，请先检查现有数据