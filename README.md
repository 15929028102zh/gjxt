# 石油工业园区设备巡检系统

## 项目简介

本系统是一个基于Spring Boot + MyBatis Plus + MySQL + Vue 3 + Element Plus的设备巡检管理系统，采用前后端分离架构，同时提供uni-app移动端应用。系统主要用于石油工业园区的设备巡检管理，包括巡检任务分配、问题记录、整改跟踪等功能。

## 整体架构分析

### 系统架构概述
本系统采用**三端分离**的架构设计：
- **后端服务**：基于Spring Boot的RESTful API服务
- **Web管理端**：基于Vue 3的后台管理系统
- **移动端**：基于uni-app的移动应用

### 技术选型分析

#### 后端技术栈 (Spring Boot 2.7.18)
- **核心框架**：Spring Boot 2.7.18 (兼容Java 1.8)
- **ORM框架**：MyBatis Plus 3.5.3.1 (简化CRUD操作)
- **数据库**：MySQL 5.1.49 (兼容性考虑)
- **连接池**：Druid 1.2.16 (阿里巴巴数据库连接池)
- **安全框架**：Spring Security (认证授权)
- **JWT**：jsonwebtoken 0.9.1 (无状态认证)
- **任务调度**：Spring Boot Quartz (定时任务)
- **API文档**：SpringDoc OpenAPI 1.6.15 (替代Swagger)
- **Excel处理**：Apache POI 5.2.3 (数据导入导出)
- **JSON处理**：FastJSON 2.0.25 (JSON序列化)
- **工具库**：Lombok 1.18.24, Apache Commons Lang3

#### 前端技术栈 (Vue 3 + Vite)
- **核心框架**：Vue 3.3.4 (Composition API)
- **构建工具**：Vite 4.4.5 (快速构建)
- **UI框架**：Element Plus 2.3.8 (企业级UI组件库)
- **图标库**：@element-plus/icons-vue 2.1.0
- **路由管理**：Vue Router 4.2.4 (History模式)
- **状态管理**：Pinia 2.1.6 (Vue 3官方推荐)
- **HTTP客户端**：Axios 1.4.0 (API请求)
- **图表库**：ECharts 6.0.0 (数据可视化)

#### 移动端技术栈 (uni-app)
- **开发框架**：uni-app (跨平台移动应用开发)
- **Vue版本**：Vue 3 (uni-app-x)
- **UI组件**：uview-plus 3.4.94, @dcloudio/uni-ui 1.5.10
- **图表组件**：ucharts (移动端图表)
- **目标平台**：Android原生应用

## 详细项目结构

```
gjxt/
├── backend/                           # 后端Spring Boot项目
│   ├── src/main/java/com/gjxt/inspection/
│   │   ├── InspectionSystemApplication.java    # 启动类
│   │   ├── common/                    # 通用类
│   │   │   ├── PageResult.java        # 分页结果封装
│   │   │   ├── Result.java            # 统一响应结果
│   │   │   └── ResultCode.java        # 响应状态码
│   │   ├── config/                    # 配置类
│   │   │   ├── CorsConfig.java        # 跨域配置
│   │   │   ├── SecurityConfig.java    # 安全配置
│   │   │   ├── MybatisPlusConfig.java # MyBatis Plus配置
│   │   │   ├── ScheduleConfig.java    # 定时任务配置
│   │   │   └── SwaggerConfig.java     # API文档配置
│   │   ├── controller/                # 控制器层
│   │   │   ├── SysUserController.java         # 用户管理
│   │   │   ├── SysDeptController.java         # 部门管理
│   │   │   ├── SysInspectionTaskController.java      # 巡检任务
│   │   │   ├── SysInspectionRecordController.java    # 巡检记录
│   │   │   ├── MonthlyInspectionTaskController.java  # 月度任务
│   │   │   ├── StatisticsController.java      # 统计分析
│   │   │   └── FileUploadController.java      # 文件上传
│   │   ├── entity/                    # 实体类
│   │   │   ├── BaseEntity.java        # 基础实体
│   │   │   ├── SysUser.java           # 用户实体
│   │   │   ├── SysDept.java           # 部门实体
│   │   │   ├── SysInspectionTask.java # 巡检任务实体
│   │   │   └── SysInspectionRecord.java # 巡检记录实体
│   │   ├── mapper/                    # 数据访问层
│   │   ├── service/                   # 业务逻辑层
│   │   │   └── impl/                  # 业务实现层
│   │   ├── task/                      # 定时任务
│   │   └── util/                      # 工具类
│   ├── src/main/resources/            # 资源文件
│   ├── target/                        # 编译输出
│   └── pom.xml                        # Maven配置
├── admin/                             # Web管理端(Vue 3)
│   ├── src/
│   │   ├── api/                       # API接口定义
│   │   │   ├── auth.js                # 认证相关API
│   │   │   ├── system.js              # 系统管理API
│   │   │   ├── inspection.js          # 巡检相关API
│   │   │   └── monthly.js             # 月度任务API
│   │   ├── components/                # 公共组件
│   │   │   ├── DictSelect.vue         # 字典选择组件
│   │   │   └── DragUpload.vue         # 拖拽上传组件
│   │   ├── layout/                    # 布局组件
│   │   ├── router/                    # 路由配置
│   │   ├── utils/                     # 工具函数
│   │   │   ├── request.js             # HTTP请求封装
│   │   │   └── token.js               # Token管理
│   │   ├── views/                     # 页面组件
│   │   │   ├── dashboard/             # 仪表盘
│   │   │   ├── system/                # 系统管理
│   │   │   │   ├── user/              # 用户管理
│   │   │   │   ├── dept/              # 部门管理
│   │   │   │   └── dict/              # 字典管理
│   │   │   ├── inspection/            # 巡检管理
│   │   │   │   ├── weekly/            # 周检管理
│   │   │   │   ├── submit/            # 提交记录
│   │   │   │   └── delayed/           # 延迟处理
│   │   │   └── monthly/               # 月度管理
│   │   └── App.vue                    # 根组件
│   ├── public/                        # 静态资源
│   ├── dist/                          # 构建输出
│   └── package.json                   # 依赖配置
├── gyxj/                              # 移动端(uni-app)
│   ├── pages/                         # 页面
│   │   ├── index/                     # 首页
│   │   ├── login/                     # 登录
│   │   ├── home/                      # 主页
│   │   ├── task/                      # 任务管理
│   │   ├── record/                    # 记录管理
│   │   ├── weekly/                    # 周检
│   │   ├── monthly/                   # 月检
│   │   ├── statistics/                # 统计
│   │   └── profile/                   # 个人中心
│   ├── components/                    # 组件
│   ├── static/                        # 静态资源
│   ├── uni_modules/                   # uni-app模块
│   │   ├── lime-picker/               # 选择器组件
│   │   ├── tui-xechars/               # 图表组件
│   │   └── wht-datetime-picker/       # 日期时间选择器
│   ├── config/                        # 配置
│   ├── utils/                         # 工具
│   ├── manifest.json                  # 应用配置
│   └── pages.json                     # 页面配置
├── docs/                              # 文档
│   └── database.sql                   # 数据库脚本
└── logs/                              # 日志文件
```

## 架构设计特点

### 1. 分层架构设计
- **表现层 (Presentation Layer)**：Vue 3前端 + uni-app移动端
- **业务层 (Business Layer)**：Spring Boot服务层
- **数据访问层 (Data Access Layer)**：MyBatis Plus + MySQL
- **基础设施层 (Infrastructure Layer)**：Spring Security + JWT + Quartz

### 2. 前后端分离
- **API优先设计**：后端提供RESTful API，前端通过HTTP调用
- **无状态认证**：使用JWT Token实现跨端认证
- **统一数据格式**：Result统一响应格式，便于前端处理

### 3. 移动端适配
- **跨平台开发**：uni-app一套代码适配多端
- **原生性能**：支持编译为Android原生应用
- **离线能力**：本地数据缓存，支持离线操作

### 4. 数据库设计
- **业务模块化**：按功能模块划分表结构
- **审计字段**：BaseEntity提供创建时间、更新时间等审计信息
- **字典管理**：sys_dictionary表统一管理系统字典数据

## 核心业务模块

### 1. 用户权限管理
- **用户管理**：用户信息、角色分配、权限控制
- **部门管理**：组织架构管理，支持树形结构
- **字典管理**：系统配置项统一管理

### 2. 巡检任务管理
- **任务配置**：定时生成巡检任务规则
- **任务分配**：按部门、人员分配巡检任务
- **任务执行**：移动端接收和执行巡检任务

### 3. 问题记录与跟踪
- **问题记录**：现场拍照、问题描述、位置信息
- **问题分类**：专业分类、部位分类、扣分性质
- **整改跟踪**：问题分配、整改确认、复查验收

### 4. 统计分析
- **数据统计**：各类问题统计、趋势分析
- **图表展示**：ECharts可视化展示
- **报表导出**：Excel格式数据导出

## 技术亮点

### 1. 现代化技术栈
- **Vue 3 Composition API**：更好的逻辑复用和类型推导
- **Vite构建工具**：快速的开发体验和构建速度
- **Pinia状态管理**：Vue 3官方推荐的状态管理方案
- **Element Plus**：成熟的企业级UI组件库

### 2. 开发效率优化
- **MyBatis Plus**：简化CRUD操作，提供代码生成器
- **Lombok**：减少样板代码，提高开发效率
- **SpringDoc OpenAPI**：自动生成API文档
- **热重载支持**：开发环境自动重启和热更新

### 3. 生产环境考虑
- **连接池优化**：Druid连接池，提供监控和性能优化
- **安全防护**：Spring Security + JWT双重保障
- **日志管理**：完善的日志记录和文件管理
- **定时任务**：Quartz调度器，支持集群部署

### 4. 移动端优势
- **原生性能**：uni-app编译为原生应用
- **丰富组件**：lime系列组件、图表组件等
- **网络适配**：支持多种网络环境配置
- **离线缓存**：本地数据存储和同步机制

## 主要功能模块

### 后台管理系统功能
1. **专业配置** - 管理设备所属专业（电气、机械、仪表等）
2. **发生部位配置** - 配置设备问题部位（压缩机、管道、阀门等）
3. **扣分性质与分数配置** - 设置扣分性质及对应分数
4. **巡检任务配置** - 配置定时生成巡检任务规则
5. **用户管理** - 管理系统用户信息和角色权限

### 移动端功能
1. **巡检任务接收** - 接收和查看巡检任务
2. **巡检记录** - 拍照记录问题，选择专业部位，录入相关信息
3. **待整改问题查询** - 查看和处理待整改问题
4. **延迟整改问题查询** - 查看超时未整改问题
5. **复查确认** - 复查员确认整改完成

### 汇总页面功能
1. **数据统计** - 统计各状态问题数量和分布
2. **查询与导出** - 条件查询和Excel导出
3. **图表展示** - 使用ECharts展示统计图表

## 数据库表结构

- `sys_user` - 用户表
- `sys_profession` - 专业表
- `sys_location` - 发生部位表
- `sys_deduction_type` - 扣分性质表
- `sys_inspection_task` - 巡检任务表
- `sys_inspection_record` - 巡检记录表

## 开发环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- HBuilderX (uniapp开发)

## 快速开始

### 后端启动
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

### 移动端开发
使用HBuilderX打开mobile目录进行开发和调试

## 部署说明

详细的部署说明请参考docs目录下的相关文档。