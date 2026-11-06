# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

石油工业园区设备巡检系统，采用 Spring Boot + MyBatis Plus + MySQL + Vue 3 + Element Plus + uni-app 的三端分离架构，包括：
- **后端服务** (`backend/`)：Spring Boot RESTful API
- **Web管理端** (`admin/`)：Vue 3 + Element Plus 后台管理
- **移动端** (`gyxj/`)：uni-app 移动应用

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.7.18 (Java 8) |
| ORM | MyBatis Plus 3.5.3.1 |
| 数据库 | MySQL 5.1.49 |
| 连接池 | Druid 1.2.16 |
| 安全 | Spring Security + JWT |
| 前端框架 | Vue 3.3.4 + Vite 4.4.5 |
| UI库 | Element Plus 2.3.8 |
| 状态管理 | Pinia 2.1.6 |
| 移动端 | uni-app (Vue 3) |

## 构建与运行

### 后端
```bash
cd backend
mvn clean install
mvn spring-boot:run
# 运行于 http://localhost:8081/api
# API文档: http://localhost:8081/api/swagger-ui/index.html
# Druid监控: http://localhost:8081/api/druid (admin/123456)
```

### Web管理端
```bash
cd admin
npm install
npm run dev     # 开发服务器 (端口5173，代理到后端8081)
npm run build   # 生产构建
```

### 移动端
使用 HBuilderX 打开 `gyxj/` 目录进行开发和调试。

## 架构设计

### 后端包结构
```
com.gjxt.inspection/
├── common/          # Result统一响应、PageResult分页、ResultCode状态码
├── config/          # SecurityConfig、CorsConfig、MybatisPlusConfig、SwaggerConfig、WebSocketConfig
├── controller/      # REST控制器（SysUser、SysDept、SysInspectionTask/Record、MonthlyInspection等）
├── entity/          # 实体类（BaseEntity提供id、deleted、createTime、updateTime）
├── mapper/          # MyBatis Mapper接口
├── service/impl/    # ServiceImpl extends ServiceImpl<Mapper, Entity> (MyBatis Plus CRUD模式)
├── task/           # Quartz定时任务
└── util/           # JwtUtil、PasswordUtil、UserContextUtil
```

### 前端结构
```
admin/src/
├── api/             # Axios API接口定义（auth.js、system.js、inspection.js、monthly.js等）
├── components/      # DictSelect字典下拉、DragUpload图片上传、NotificationPopup通知弹窗
├── layout/          # 主布局（侧边栏、Header、Tab导航）
├── router/          # Vue Router（History模式，Token验证守卫）
├── stores/          # Pinia stores（user用户信息、websocket WebSocket连接）
├── utils/           # request.js(Axios封装+拦截器)、token.js、permission.js
└── views/           # 页面组件（home、login、system、inspection、monthly等模块）
```

### API设计
- **Base URL**: `http://{host}:8081/api`
- **Context Path**: `/api`
- **认证**: `POST /auth/login` → JWT Token (24h有效期)
- **上传**: `POST /upload/image` → 返回 `{ filePath: "/files/images/..." }`
- **文件访问**: `GET /files/**`

关键端点：
| 模块 | 端点 |
|------|------|
| 用户 | `/users` (CRUD) |
| 部门 | `/depts` |
| 字典 | `/dictionaries/{type}` |
| 周检任务 | `/inspection-task` |
| 周检记录 | `/inspection-record/page`, `POST /submit`, `PUT /{id}/rectify`, `PUT /{id}/review` |
| 月检任务 | `/monthly-tasks` |
| 月检记录 | `/monthly-inspection-record/page`, `POST .../rectify`, `POST .../review` |
| 通知 | `/notifications/user/{userId}`, `/notifications/department/{deptId}/tasks` |

## 数据库设计

- **逻辑删除**: 所有实体使用 `deleted` 字段 (0=有效, 1=已删除)
- **审计字段**: BaseEntity 提供 `id`(UUID)、`createTime`、`updateTime`
- **状态字段**: 巡检记录 `status` (0:待整改, 1:待复查, 2:已完成, 3:待审核)
- **权限控制**: 用户 `is_admin`、`weekly_permission`、`monthly_permission` 字段

## WebSocket实时通知

- **端点**: `/ws` (STOMP over SockJS)
- **个人订阅**: `/user/queue/notifications`
- **广播订阅**: `/topic/notifications`
- **重连**: 5秒延迟自动重连
- **通知触发**: 全局事件 `open-notifications` 打开通知弹窗

## 关键配置

| 文件 | 用途 |
|------|------|
| `backend/src/main/resources/application.yml` | 数据库连接、JWT密钥、文件上传路径、定时任务配置 |
| `admin/vite.config.js` | 开发代理配置（/api → localhost:8081）、路径别名 |
| `admin/.env.development` | 开发环境API基础URL |
| `gyxj/manifest.json` | App ID、Android配置、网络安全域 |

## 开发注意事项

- **CORS**: 后端 `SecurityConfig` 配置允许所有来源
- **文件存储**: 上传文件存储在 `D:/gjxt/backend/uploads/`，通过 `/files/**` 访问
- **定时任务**: 延迟检查 Cron 表达式 `delay-check-cron: "0 0 1 * * ?"` (每日1点)
- **无单元测试**: 后端项目中无 test 目录

## 常见任务

### 后端热重启
修改后端代码后 `mvn spring-boot:run` 会自动重载。

### 前端API调试
开发环境 Admin 已配置代理，无需修改 Vite 即可调试。

### 添加新的API接口
1. 在对应 `Mapper.xml` 添加 SQL
2. 在 `*ServiceImpl` 添加业务逻辑
3. 在 `*Controller` 添加 REST 端点
4. 重启后端后访问 Swagger 文档验证
