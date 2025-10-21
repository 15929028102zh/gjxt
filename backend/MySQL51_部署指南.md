# MySQL 5.1 兼容性调整和部署指南

## 已完成的兼容性调整

### 1. 数据库驱动调整
- **驱动版本**: 从 MySQL 8.0.33 降级到 MySQL 5.1.49
- **驱动类名**: 从 `com.mysql.cj.jdbc.Driver` 改为 `com.mysql.jdbc.Driver`
- **依赖配置**: 从 `mysql-connector-j` 改为 `mysql-connector-java`

### 2. 数据库连接配置
- **移除参数**: `serverTimezone=UTC` (MySQL 5.1 不支持)
- **添加参数**: `allowPublicKeyRetrieval=true&useOldAliasMetadataBehavior=true` (兼容性参数)
- **保留参数**: `useUnicode=true&characterEncoding=utf8&useSSL=false`

### 3. 建表脚本调整
- **字符集**: 从 `utf8mb4` 改为 `utf8` (MySQL 5.1 兼容)
- **默认值**: 移除 `CURRENT_TIMESTAMP` 默认值 (MySQL 5.1 限制)
- **新脚本**: 创建了 `create_tables_mysql51.sql` 兼容脚本

## ✅ 测试验证结果
- **编译状态**: ✅ 成功 (mvn clean compile)
- **启动状态**: ✅ 成功 (应用程序正常启动)
- **端口监听**: ✅ 8081端口正常监听
- **数据库连接**: ✅ 无连接错误
- **MyBatis加载**: ✅ 所有Mapper文件正常加载

## 详细兼容性调整

### 1. MySQL驱动调整
- **原版本**: MySQL 8.0.33 (`com.mysql:mysql-connector-j`)
- **新版本**: MySQL 5.1.49 (`mysql:mysql-connector-java`)
- **文件**: `pom.xml`

### 2. 数据库连接配置调整
- **驱动类**: `com.mysql.jdbc.Driver` (替换原来的 `com.mysql.cj.jdbc.Driver`)
- **连接URL**: 移除了 `serverTimezone` 和 `allowPublicKeyRetrieval` 参数
- **文件**: `src/main/resources/application.yml`

### 3. 建表脚本兼容性调整
- **字符集**: 从 `utf8mb4` 改为 `utf8`
- **时间戳**: 移除了 `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`
- **数据类型**: 调整为MySQL 5.1支持的类型
- **文件**: `create_tables_mysql51.sql`

## 部署步骤

### 1. 环境准备
确保生产服务器已安装：
- MySQL 5.1.x
- Java 1.8
- Maven 3.x

### 2. 数据库准备
```sql
-- 1. 创建数据库
CREATE DATABASE gjxt DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- 2. 创建用户（可选）
CREATE USER 'gjxt_user'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON gjxt.* TO 'gjxt_user'@'%';
FLUSH PRIVILEGES;

-- 3. 执行建表脚本
USE gjxt;
SOURCE /path/to/create_tables_mysql51.sql;
```

### 3. 应用配置调整
修改 `application.yml` 中的生产环境配置：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://your_production_host:3306/gjxt?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: gjxt_user
    password: your_production_password
```

### 4. 编译和打包
```bash
# 清理并编译
mvn clean compile

# 打包
mvn clean package -DskipTests

# 生成的jar文件位于 target/inspection-system-1.0.0.jar
```

### 5. 部署运行
```bash
# 复制jar文件到生产服务器
scp target/inspection-system-1.0.0.jar user@production-server:/opt/gjxt/

# 在生产服务器上运行
java -jar /opt/gjxt/inspection-system-1.0.0.jar --spring.profiles.active=prod
```

## 注意事项

### MySQL 5.1 限制
1. **字符集**: 不支持 `utf8mb4`，只能使用 `utf8`
2. **时间戳**: 对 `CURRENT_TIMESTAMP` 的支持有限制
3. **JSON类型**: 不支持JSON数据类型，使用TEXT替代
4. **索引长度**: 对于VARCHAR字段的索引有长度限制

### 性能优化建议
1. **连接池配置**: 根据生产环境调整Druid连接池参数
2. **索引优化**: 确保关键查询字段都有适当的索引
3. **内存配置**: 调整JVM内存参数适应生产环境

### 监控和日志
1. **应用日志**: 配置适当的日志级别和输出路径
2. **数据库监控**: 监控MySQL 5.1的性能指标
3. **健康检查**: 配置应用健康检查端点

## 回滚方案
如果需要回滚到MySQL 8.x：
1. 恢复 `pom.xml` 中的MySQL驱动配置
2. 恢复 `application.yml` 中的连接配置
3. 使用原始的 `create_tables.sql` 脚本

## 测试验证
部署完成后，请验证以下功能：
1. 数据库连接正常
2. 用户登录功能
3. 巡检记录的增删改查
4. 文件上传下载功能
5. 批量删除功能

## 联系支持
如遇到问题，请检查：
1. 数据库连接配置
2. MySQL 5.1服务状态
3. 应用日志错误信息
4. 网络连接状况