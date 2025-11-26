# 石油工业园区设备巡检系统 - 后端

## 项目简介

石油工业园区设备巡检系统是一个基于Spring Boot + MyBatis Plus + MySQL的企业级巡检管理系统，主要用于石油工业园区的设备巡检、问题记录、整改跟踪等业务场景。

## 技术栈

- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis Plus 3.5.3.1
- **连接池**: Druid 1.2.16
- **安全**: Spring Security + JWT
- **构建工具**: Maven 3.6+
- **JDK版本**: 1.8+

## 功能模块

### 1. 用户管理
- 用户登录/登出
- 用户信息管理
- 角色权限控制
- 密码管理

### 2. 基础数据管理
- 专业管理
- 发生部位管理
- 扣分性质管理

### 3. 巡检任务管理
- 任务创建与分配
- 任务执行跟踪
- 任务完成确认

### 4. 巡检记录管理
- 问题记录与上报
- 整改跟踪
- 复查确认
- 图片上传

### 5. 统计分析
- 问题统计分析
- 数据可视化
- 报表导出

### 6. 文件管理
- 文件上传
- 图片管理
- 文件删除

## 项目结构

```
src/main/java/com/gjxt/inspection/
├── common/           # 公共类
│   ├── PageResult.java      # 分页结果
│   ├── Result.java          # 统一返回结果
│   └── ResultCode.java      # 返回码枚举
├── config/           # 配置类
│   ├── FileUploadConfig.java    # 文件上传配置
│   ├── JwtConfig.java           # JWT配置
│   ├── MyBatisPlusConfig.java   # MyBatis Plus配置
│   ├── ScheduleConfig.java      # 定时任务配置
│   └── SwaggerConfig.java       # Swagger配置
├── controller/       # 控制器
│   ├── FileUploadController.java        # 文件上传控制器
│   ├── StatisticsController.java        # 统计分析控制器
│   ├── SysDeductionTypeController.java  # 扣分性质控制器
│   ├── SysInspectionRecordController.java # 巡检记录控制器
│   ├── SysInspectionTaskController.java   # 巡检任务控制器
│   ├── SysLocationController.java         # 发生部位控制器
│   ├── SysProfessionController.java      # 专业控制器
│   └── SysUserController.java            # 用户控制器
├── entity/           # 实体类
│   ├── SysDeductionType.java    # 扣分性质实体
│   ├── SysInspectionRecord.java # 巡检记录实体
│   ├── SysInspectionTask.java   # 巡检任务实体
│   ├── SysLocation.java         # 发生部位实体
│   ├── SysProfession.java       # 专业实体
│   └── SysUser.java             # 用户实体
├── mapper/           # Mapper接口
│   ├── SysDeductionTypeMapper.java
│   ├── SysInspectionRecordMapper.java
│   ├── SysInspectionTaskMapper.java
│   ├── SysLocationMapper.java
│   ├── SysProfessionMapper.java
│   └── SysUserMapper.java
├── service/          # 服务接口
│   ├── impl/         # 服务实现类
│   └── I*.java       # 服务接口
├── task/             # 定时任务
│   └── InspectionTask.java
├── util/             # 工具类
│   ├── FileUploadUtil.java  # 文件上传工具
│   ├── JwtUtil.java         # JWT工具
│   └── PasswordUtil.java    # 密码工具
└── InspectionApplication.java # 启动类
```

## 快速开始

### 1. 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+ (可选)

### 2. 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE gjxt_inspection DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```bash
mysql -u root -p gjxt_inspection < src/main/resources/sql/init.sql
```

### 3. 配置文件

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gjxt_inspection?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: your_password
```

### 4. 启动项目

```bash
# 编译项目
mvn clean compile

# 启动项目
mvn spring-boot:run
```

或者使用IDE直接运行 `InspectionApplication.java`

### 5. 访问系统

- 系统地址：http://localhost:8081/api
- Swagger文档：http://localhost:8081/api/swagger-ui.html
- Druid监控：http://localhost:8081/api/druid/index.html

### 6. 默认账号

- 管理员账号：admin / admin123
- 测试账号：zhangsan / 123456

## API接口

### 用户管理
- POST `/user/login` - 用户登录
- GET `/user/info` - 获取当前用户信息
- GET `/user/page` - 分页查询用户
- POST `/user` - 新增用户
- PUT `/user` - 更新用户
- DELETE `/user/{id}` - 删除用户

### 巡检记录
- GET `/record/page` - 分页查询巡检记录
- GET `/record/{id}` - 查询记录详情
- POST `/record` - 新增巡检记录
- PUT `/record` - 更新巡检记录
- POST `/record/confirm-rectification` - 整改确认
- POST `/record/confirm-review` - 复查确认

### 统计分析
- GET `/statistics/summary` - 获取汇总统计
- GET `/statistics/status` - 获取状态统计
- GET `/statistics/profession` - 获取专业统计
- GET `/statistics/location` - 获取部位统计

### 文件上传
- POST `/upload/image` - 上传图片
- POST `/upload/file` - 上传文件
- DELETE `/upload/file` - 删除文件

## 部署说明

### 1. 打包项目

```bash
mvn clean package -Dmaven.test.skip=true
```

### 2. 运行JAR包

```bash
java -jar target/inspection-system-1.0.0.jar
```

### 3. 配置文件外置

```bash
java -jar target/inspection-system-1.0.0.jar --spring.config.location=application.yml
```

## 注意事项

1. 确保MySQL数据库版本为8.0+
2. 文件上传路径需要有写权限
3. JWT密钥建议在生产环境中修改
4. 定时任务默认每天凌晨1点执行延迟整改检查
5. 建议在生产环境中配置Redis作为缓存

## 开发规范

1. 所有API接口都有统一的返回格式
2. 使用MyBatis Plus进行数据库操作
3. 统一异常处理和日志记录
4. 代码注释完整，符合JavaDoc规范
5. 数据库字段采用下划线命名，Java字段采用驼峰命名

## 联系方式

如有问题，请联系开发团队。
### 2025-11-14 修复：月检整改/复查字段缺失

问题：后端实体 `sys_monthly_inspection_record` 包含整改/复查字段，但数据库表缺少对应列，导致查询报 `Unknown column 'rectification_suggestion'`。

处理：
- 新增 `alter_monthly_inspection_record_add_rectify_review.sql`，为表添加以下列：
  - `rectification_suggestion`、`rectification_effect`、`rectification_images`、`rectification_time`
  - `review_suggestion`、`review_effect`、`review_images`、`review_time`
- 更新 `monthly_inspection_record_table.sql`（建表DDL）与 `MonthlyInspectionRecordMapper.xml`（ResultMap与Base_Column_List）。

执行方式（MySQL）：
- 在目标库执行：
  - `source backend/alter_monthly_inspection_record_add_rectify_review.sql`
  - 或在SQL客户端复制执行其中的 `ALTER TABLE` 语句。

注意：若已有历史数据，此脚本仅新增列，不会影响现有数据；新列为可空，兼容旧记录。