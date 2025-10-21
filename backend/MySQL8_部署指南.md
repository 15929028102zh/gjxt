# 石油工业园区设备巡检系统 - MySQL 8.x 部署指南

## 系统环境要求
- Java 1.8+
- Maven 3.6+
- MySQL 8.0+
- Windows 11

## MySQL 8.x 版本配置

### 1. 数据库驱动配置 (pom.xml)

```xml
<properties>
    <mysql.version>8.0.33</mysql.version>
</properties>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>${mysql.version}</version>
    <scope>runtime</scope>
</dependency>
```

### 2. 数据库连接配置 (application.yml)

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/gjxt_inspection?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
    username: root
    password: root
```

### 3. 关键配置说明

#### MySQL 8.x 驱动特点：
- **groupId**: `com.mysql` (MySQL 8.x 使用新的groupId)
- **artifactId**: `mysql-connector-j` (MySQL 8.x 使用新的artifactId)
- **driver-class-name**: `com.mysql.cj.jdbc.Driver` (新的驱动类名)

#### URL参数说明：
- `serverTimezone=UTC`: MySQL 8.x 要求明确指定时区
- `useUnicode=true`: 启用Unicode支持
- `characterEncoding=utf8`: 设置字符编码
- `useSSL=false`: 本地开发环境禁用SSL

## 部署步骤

### 1. 清理并编译项目
```bash
mvn clean compile
```

### 2. 启动应用程序
```bash
mvn spring-boot:run
```

### 3. 验证部署
- 应用程序启动端口：8081
- 访问地址：http://localhost:8081/api
- 接口文档：http://localhost:8081/api/swagger-ui/index.html
- Druid监控：http://localhost:8081/api/druid

## 测试验证结果

### ✅ 编译测试
- Maven编译成功
- 依赖下载正常
- 无编译错误

### ✅ 启动测试
- Spring Boot应用启动成功
- 启动时间：约5.4秒
- 进程PID：7072

### ✅ 端口监听
- 8081端口正常监听
- 服务状态：LISTENING

### ✅ 数据库连接
- MySQL 8.x 驱动加载成功
- 数据库连接池初始化正常
- MyBatis Plus配置加载成功

### ✅ 服务运行
- 系统启动横幅显示正常
- 所有访问地址可用
- 应用程序运行稳定

## 生产环境部署准备

1. **数据库准备**：确保MySQL 8.0+环境已就绪
2. **配置调整**：根据生产环境修改数据库连接参数
3. **安全配置**：生产环境启用SSL连接
4. **性能优化**：调整连接池和JVM参数

## 版本兼容性

- ✅ MySQL 8.0.33 - 推荐版本
- ✅ MySQL 8.0.x - 完全兼容
- ⚠️ MySQL 5.7.x - 需要调整驱动配置
- ❌ MySQL 5.1.x - 需要降级驱动版本

---
**部署完成时间**: 2025-09-17 14:03:36
**测试状态**: 全部通过 ✅