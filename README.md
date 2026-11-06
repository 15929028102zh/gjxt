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

## 更新记录

- 2025-11-18
  - 移动端月检录入级联修复：模块→责任、责任→问题按名称过滤，责任与问题列表正确加载（`gyxj/pages/monthly/task-record.uvue`）。
  - Web端新增月检记录级联优化：按专业名称过滤模块，按模块名称过滤责任，按责任名称过滤问题，质量信息合并回填（`admin/src/views/inspection/monthly/components/AddDialog.vue`、`admin/src/api/monthlyRecord.js`）。
  - 安全记分规则统一：由性质界定驱动，重要=5、一般=3、观察=1；问题选择与性质界定变更均自动回写（Web：`AddDialog.vue`，移动端：`task-record.uvue`）。
  - 确认整改弹窗性质界定显示优化：使用字典中文名称展示，支持 qualityId/名称兼容映射（`admin/src/views/inspection/monthly/components/RectificationDialog.vue`）。
  - 搜索参数健壮化：修复 `recordStatus=NaN` 导致 400，空值不传参（`admin/src/views/inspection/monthly/index.vue`）。
  - 图片上传补齐：新增“确认整改”与“确认复查”页面的图片上传与提交（`RectificationConfirmDialog.vue`、`ReviewDialog.vue`）。
  - 列表显示调整：将“状态”移动到操作列标签展示（`admin/src/views/inspection/monthly/index.vue`）。
  - UTS编译修复：`getScoreByQualityName` 判空与 `indexOf` 使用规范，兼容 UTS 语法（`gyxj/pages/monthly/task-record.uvue`）。
  - 写表字段统一：移动端保存改为使用任务ID `taskId`，其余字段与 Web 对齐（`gyxj/pages/monthly/task-record.uvue`）。
  - 复查接口统一：移动端复查改用 `PUT /monthly-inspection-record/{id}/review` 并按 Web 字段提交（`gyxj/pages/record/review.uvue`）。
  - 新增月检查看页：`gyxj/pages/monthly/view.uvue`，并在移动端列表已复查状态跳转到该查看页（`gyxj/pages/record/record.uvue`）。
  - 移动端确认整改页优化：支持滚动，字段对齐Web端并美化页面（`gyxj/pages/monthly/rectify.uvue`）。
  - 移动端确认整改页补齐字段：任务编号、月检时间、部门、专业、责任要素、检查内容、检查组长、副组长（`gyxj/pages/monthly/rectify.uvue`）。
  - 移动端确认复查页对齐Web：补齐任务编号、月检时间、部门、专业、责任要素、检查内容、检查人、组长/副组长；复查提交字段统一为 `reviewSuggestion`、`reviewImages`（`gyxj/pages/monthly/review.uvue`）。
  - 月检延迟整改：移动端月检页新增“延迟整改”跳转，按当前日期筛选整改期限小于该日期的记录，管理员不按部门过滤；复用问题列表并跳待整改页；后端分页接口兼容 `rectificationDeadlineBefore` 参数（`gyxj/pages/monthly/monthly.uvue`、`gyxj/pages/record/record.uvue`、`backend/.../MonthlyInspectionRecordController.java`）。
  - 月检待整改数量排除延迟整改：统计时额外请求延迟整改数量并从待整改数量中扣除（`gyxj/pages/monthly/monthly.uvue`）。
  - 月检待整改列表过滤修复：与后端实体字段对齐，使用 `rectificationDate` 进行过期过滤，确保列表与统计一致（`gyxj/pages/record/record.uvue`）。
  - 延迟整改过滤增强：统一日期为`yyyy-MM-dd`并改为数值比较，避免字符串比较误判（`gyxj/pages/record/record.uvue`）。
  - 移动端首页精简：移除“月度巡检趋势”及以下内容，聚焦统计与快捷入口（`gyxj/pages/home/home.uvue`）。
  - 月检“新增记录”交互增强：点击后弹出本部门“未开始/进行中”的月检任务选择，选择后按通知栏逻辑跳转并打开新增弹窗（`admin/src/views/inspection/monthly/index.vue`）。
  - Web端“新增”复用通知弹窗：点击后打开通知弹窗并默认定位到“月检任务”列表（`admin/src/layout/index.vue`、`admin/src/views/inspection/monthly/index.vue`）。
  - 通知弹窗触发增强：页面通过全局事件 `open-notifications` 打开弹窗并切换标签，避免跨组件方法不可用导致无法弹出（`admin/src/layout/index.vue`、`admin/src/views/inspection/monthly/index.vue`）。
  - 移除月检页本地任务选择弹窗：统一复用通知弹窗的“月检任务”标签，不再在页面内弹自定义选择框（`admin/src/views/inspection/monthly/index.vue`）。
  - Web端月检管理新增“延迟整改”页面：查询整改期限小于当前日期的月检记录，支持部门筛选（管理员可选）、查看详情、确认整改、确认复查（`admin/src/views/monthly/delayed/index.vue`、`admin/src/router/index.js`）。
  - 月检延迟整改页面仅显示“延迟天数>0”的记录（`admin/src/views/monthly/delayed/index.vue`）。
  - 月检延迟整改页面字段顺序与月检录入页一致，并复用查看/整改/复查功能（`admin/src/views/monthly/delayed/index.vue`）。
  - 月检延迟整改页面仅统计“待整改且延迟天数>0”的记录，避免状态为“已整改/已复查”的记录进入列表（`admin/src/views/monthly/delayed/index.vue`）。
  - 移动端月检延迟整改逻辑与Web对齐：仅显示“待整改且整改期限小于当天”的记录，统一字段解析与过滤（`gyxj/pages/record/record.uvue`）。
  - 移动端月检待整改列表与统计一致：待整改列表排除无整改期限或非法日期的记录，总数按过滤后列表计算（`gyxj/pages/record/record.uvue`）。
  - 移动端从月检页“待整改”跳转列表时携带 `deadlineFrom=今日` 参数，列表按“整改期限≥今日”的规则过滤延迟问题（`gyxj/pages/monthly/monthly.uvue`、`gyxj/pages/record/record.uvue`）。
  - Web端“新增月检记录”按钮复用通知的任务加载逻辑，使用任务通知接口获取任务数量与列表（`admin/src/views/inspection/monthly/index.vue`、`admin/src/layout/index.vue`、`admin/src/stores/websocket.js`）。
  - 后端支持按整改期限筛选月检记录：`GET /monthly-inspection-record/page` 增加 `rectificationDeadlineBefore` 参数过滤（`backend/src/main/resources/mapper/MonthlyInspectionRecordMapper.xml`）。
  - 移动端月检页UTS编译错误修复：`openTaskPicker/closeTaskPicker/pickTaskAndGo` 误置于 `</style>` 之后导致模板解析异常，现已移入 `<script lang="uts">` 方法块并移除重复段（`gyxj/pages/monthly/monthly.uvue`）。
  - 移动端月检任务选择弹窗样式修复：使用居中蒙层弹窗展示，避免在页面底部显示（`gyxj/pages/monthly/monthly.uvue`）。

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

## 功能修复记录

- 前端：月检“确认整改”上传路径统一修复（2025-11-14）
  - 现象：数据库中 `rectificationImages` 保存为 `blob:` 开头的本地URL，详情弹窗无法显示整改图片。
  - 原因：旧实现使用原生 `el-upload`，未走后端上传接口，直接提交了本地 `blob` 路径。
  - 修复：`RectificationConfirmDialog.vue` 替换为统一的 `DragUpload` 组件，上传至后端 `/upload/image` 接口并保存相对路径（逗号分隔）。
  - 验证：上传后网络面板返回 `filePath` 相对路径；详情弹窗 `parseImageUrls` 能正确拼接并显示。

- 前端：月检详情弹窗补充显示责任人/责任单位（2025-11-14）
  - 现象：新增月检记录时填写了“责任人/责任单位”，在详情弹窗中未显示，易被误判为未保存。
  - 原因：`ViewDialog.vue` 基本信息段未包含这两项字段展示。
  - 修复：在 `ViewDialog.vue` 基本信息 `el-descriptions` 中新增“责任人”和“责任单位”的展示，使用 `formData.responsiblePerson` 与 `formData.responsibleUnit`。
  - 验证：本地启动前端后，打开月检记录详情，能看到责任人与责任单位正常显示；列表列 `responsiblePerson/responsibleUnit` 保持一致。

### 2025-01-31 移动端月检任务通知功能分析

**问题描述：**
移动端月检任务通知显示为空，而Web端正常显示数据。

**分析过程：**
1. **API接口对比**：Web端和移动端都使用相同的API接口 `/notifications/user/${userId}/tasks`
2. **API基础URL**：两端都使用相同的后端地址 `http://192.168.76.128:8081`
3. **数据处理逻辑**：移动端和Web端的数据处理逻辑基本一致
4. **后端实现**：后端返回 `ResponseEntity<List<Notification>>`，直接是通知数组

**根本原因：**
经过详细分析，发现后端SQL查询 `selectTaskNotificationsByUserId` 方法按部门ID过滤月检任务通知，这是**原始需求设计**，不是问题。月检任务通知应该按部门显示，同一部门的用户可以看到该部门的所有月检任务。

**修复内容：**
1. 在移动端 `loadTaskNotifications` 方法中添加了详细的日志输出，便于调试
2. 确认了移动端数据处理逻辑正确，能够正确解析后端返回的通知数组

**技术要点：**
- 后端使用 `UserContextUtil.getCurrentUserDeptId()` 获取当前用户部门ID
- SQL查询条件：`t.department_id = #{departmentId}` 按部门过滤
- 移动端通过 `uni.getStorageSync('token')` 获取认证token
- 数据字段映射：`id`, `taskId`, `taskNo`, `title`, `content`, `createTime`, `status`, `inspectionMonth`

**注意事项：**
- 月检任务通知是按部门维度显示的，这是业务需求，不是技术问题
- 如果某个部门没有月检任务或任务未设置 `notification_sent = 1`，则不会显示通知
- 移动端和Web端的认证机制一致，都使用JWT Bearer Token

### 2025-01-31 移动端首页和周检页面数据不一致修复

**问题描述：**
移动端首页的周检数据和周检页面的数据不一致，管理员用户在两个页面看到的数据不同。

**分析过程：**
1. **首页数据获取**：使用 `/inspection-record/page` 接口，包含管理员权限判断
2. **周检页面数据获取**：使用相同的 `/inspection-record/page` 接口，但缺少管理员权限判断
3. **权限逻辑差异**：
   - 首页：管理员用户不添加部门过滤，可查看所有部门数据
   - 周检页面：所有用户都添加部门过滤，只能查看本部门数据

**根本原因：**
周检页面缺少管理员权限判断逻辑，导致管理员用户也被限制在本部门数据范围内。

**修复内容：**
1. 在周检页面的 `loadStatistics()` 方法中添加管理员权限判断
2. 在周检页面的 `loadRecentRecords()` 方法中添加管理员权限判断
3. 统一部门过滤逻辑：`(!isAdmin && departmentId != null && departmentId != '') ? '&departmentId=' + departmentId : ''`

**技术要点：**
- 管理员权限判断：`isAdmin = (parsedUserInfo.getNumber('isAdmin') ?? 0) === 1`
- 部门过滤条件：管理员用户不添加 `departmentId` 参数，普通用户添加部门过滤
- API接口统一：两个页面都使用 `/inspection-record/page` 接口
- 状态过滤：`status=0`（待整改）、`status=1`（已整改）、`status=2`（已复查）

**修复后效果：**
- 管理员用户在首页和周检页面看到的数据保持一致（全部门数据）
- 普通用户在两个页面看到的数据保持一致（本部门数据）
- 数据权限控制逻辑统一，符合业务需求
## 变更记录（2025-11-14）

- 修复“月检-整改确认失败（Unknown column 'rectification_suggestion'）”问题：
  - 原因：实体 `sys_monthly_inspection_record` 缺少整改与复查相关列，MyBatis-Plus在 `getById` 等查询中会按实体字段生成SQL（如 `rectification_suggestion`），导致查询报错。
  - 处理：
    - 新增数据库脚本 `backend/alter_monthly_inspection_record_add_rectify_review.sql`，为表 `sys_monthly_inspection_record` 添加以下字段：
      - `rectification_suggestion`、`rectification_effect`、`rectification_images`、`rectification_time`
      - `review_suggestion`、`review_effect`、`review_images`、`review_time`
    - 更新 `backend/monthly_inspection_record_table.sql`（建表DDL）与 `backend/src/main/resources/mapper/MonthlyInspectionRecordMapper.xml`（ResultMap与Base_Column_List）保持一致。

- 执行步骤（Windows/MySQL）：
  - 登录数据库后执行：
    - `source backend/alter_monthly_inspection_record_add_rectify_review.sql`
  - 或在客户端直接运行该文件中的 `ALTER TABLE ...` 语句。

提示：若使用初始化建库，请使用更新后的 `backend/monthly_inspection_record_table.sql`，避免新库缺列。

### 前端：月检“查看详情”弹窗显示整改/复查信息修复（2025-11-14）
- 现象：确认整改后，“查看详情”弹窗未显示整改信息与图片、复查信息与图片。
- 原因：组件 `ViewDialog.vue` 使用了不存在的 `status` 字段控制显示，且未展示整改/复查文本字段。
- 修复：
  - 将显示条件改为 `recordStatus >= 1/2`。
  - 展示整改字段：`rectificationSuggestion`（整改建议）、`rectificationEffect`（整改效果）、`rectificationTime`（整改时间）。
  - 展示复查字段：`reviewSuggestion`（复查意见）、`reviewEffect`（复查效果）、`reviewTime`（复查时间）。
  - 图片仍用统一的 `parseImageUrls` 解析展示。
- 验证：启动前端后，在“月检记录录入”页面点击“查看”，应能看到对应整改与复查信息及图片。

### 前端：月检“确认整改”上传路径统一修复（2025-11-14）
- 现象：确认整改上传的图片路径存入数据库为 `blob:http://localhost:5173/...`，导致详情页无法正常展示。
- 原因：`RectificationConfirmDialog.vue` 使用原生 `el-upload` 且未走后端上传，提交时直接取本地 `blob` URL。
- 修复：
  - 替换为通用上传组件 `DragUpload`，走后端图片上传接口（`/api/upload/image`）。
  - 提交时将返回的 `filePath/url` 转换为相对路径（如 `/files/images/2025xxxx/xxx.jpg`），以逗号拼接存储到 `rectificationImages`。
  - 展示沿用 `parseImageUrls`，自动转换为可访问的完整URL。
- 验证：在“确认整改”弹窗上传图片并提交，查看“查看详情”弹窗应显示整改图片；检查数据库字段不再出现`blob:`前缀。

### 前端：复查确认弹框优化与问题信息补全（2025-11-17）
- 现象：复查确认弹框整改情况展示冗余，复查信息录入字段过多，问题信息字段缺失。
- 修复：
  - **精简整改情况展示**：仅保留整改效果描述和整改图片展示，移除整改建议等冗余字段。
  - **精简复查信息录入**：仅保留复查情况（文本输入）和复查图片上传，移除其他不必要字段。
  - **补全问题信息字段**：对比查看弹框，确保包含所有必要字段（任务编号、月检时间、检查部门、检查专业、检查组长、检查副组长、责任人、责任单位、责任要素、检查内容、性质界定、安全记分、问题描述、发现位置、检查人、整改期限、状态、创建时间）。
- 文件：`admin/src/views/inspection/monthly/components/ReviewDialog.vue`

### 移动端：月检通知接口传参修复（2025-11-17）
- 现象：移动端获取月检通知时调用接口传参不正确，使用用户ID而非部门ID。
- 修复：
  - **前端修改**：`gyxj/pages/monthly/monthly.uvue` 中 `loadTaskNotifications()` 方法，改用部门ID获取通知。
  - **逻辑优化**：管理员用户可查看所有部门通知，普通用户仅查看本部门通知。
  - **后端支持**：新增部门级通知接口：
    - `GET /notifications/department/{departmentId}/tasks` - 获取指定部门任务通知
    - `GET /notifications/department/tasks` - 获取所有部门任务通知（管理员）
  - **服务实现**：`NotificationService` 新增 `getTaskNotificationsByDepartment()` 和 `getAllTaskNotifications()` 方法。
  - **数据访问**：`NotificationMapper` 新增对应SQL查询方法。
- 验证：移动端月检页面通知功能正常，按部门正确显示任务通知。

### 移动端：月检统计参数修复与新增整改/复查页面（2025-11-17）
- 现象：待整改数、已整改数、已复查数显示一致。
- 原因：移动端统计与列表调用参数使用`status`与`departmentId`，后端实际接收`recordStatus`与`responsibilityId`。
- 修复：
  - 统计接口：改为`recordStatus=0/1/2`，部门过滤参数改为`responsibilityId`（管理员不传）。
  - 列表接口：部门过滤参数改为`responsibilityId`。
  - 文件：`gyxj/pages/monthly/monthly.uvue`。
- 新增页面（UTS）：
  - `pages/monthly/rectify.uvue`：确认整改，调用`PUT /monthly-inspection-record/{id}/rectify`，支持图片上传（最多3张）。
  - `pages/monthly/review.uvue`：确认复查，调用`PUT /monthly-inspection-record/{id}/review`，支持图片上传（最多3张）。
- 路由：在`gyxj/pages.json`新增：
  - `pages/monthly/rectify`（导航标题：确认整改）
  - `pages/monthly/review`（导航标题：确认复查）
- 验证：非管理员账号下三类数量分别正确；整改/复查提交成功后返回`code=200`，Web端详情可正确显示效果与图片。

### 月检记录字段统一：责任单位与责任人（2025-11-17）
- 诉求：月检记录使用数据库字段`responsible_unit`与`responsible_person`统一存储，前后端与移动端同步。
- 后端：
  - 新增实体字段：`MonthlyInspectionRecord.responsibleUnit`、`MonthlyInspectionRecord.responsiblePerson`（自动映射到`responsible_unit`、`responsible_person`）。
- Web端：
  - 月检录入新增/编辑弹窗表单字段已包含`责任单位/责任人`，提交保持字段名一致；列表展示保持使用`responsibleUnit/responsiblePerson`。
- 移动端：
  - 月检任务录入页面`pages/monthly/task-record.uvue`新增输入：责任单位、责任人；保存时提交`responsibleUnit/responsiblePerson`。
- 验证：新增与编辑后，列表与查看弹框均能正确显示责任单位与责任人。

### Web端：月检提交编辑弹窗组长/副组长回写修复（2025-11-17）
- 现象：编辑弹窗中“检查组长/副组长”选择框未正确回填，之前版本正常。
- 原因：当记录中仅提供任务编号且任务ID查询失败时，未能获取任务详情中的`groupLeaderId/deputyLeaderId`，导致下拉框无选中值；同时用户列表加载参数名与后端不一致（`page`应为`current`）。
- 修复：
  - 当无法获取任务详情时，依据记录返回的`groupLeaderName/deputyLeaderName`在用户选项中匹配回填ID。
  - 用户列表查询参数修正为`current=1&size=1000`。
- 文件：`admin/src/views/inspection/monthlySubmit/components/EditDialog.vue`

### Web端：月检查看与编辑弹框整改时间/整改期限回写修复（2025-11-17）
- 现象：查看弹框与编辑弹框中整改时间/整改期限未正确显示或为空。
- 原因：前端字段名与后端不完全一致，存在`rectificationDeadline/rectificationDate`与`rectificationTime`的差异，查看弹框未做兜底兼容。
- 修复：
  - 查看弹框：新增兜底方法，优先显示`rectificationDeadline`，否则显示`rectificationDate`；整改时间优先显示`rectificationTime`，否则显示`rectifyTime`。
  - 编辑弹框：只读区增加“整改时间”显示，表单初始化对`rectificationDeadline/rectificationDate`进行规范化填充。
- 文件：
  - `admin/src/views/inspection/monthly/components/ViewDialog.vue`
  - `admin/src/views/inspection/monthlySubmit/components/EditDialog.vue`

### Web端：复查弹框整改信息回写兼容（2025-11-17）
- 现象：后端仅返回`rectificationSuggestion`（例如“这是一个测试整改建议”），复查弹框“整改效果”未显示。
- 原因：复查弹框仅展示`rectificationEffect`字段，未对`rectificationSuggestion`做兼容映射。
- 修复：当`rectificationEffect`为空时，自动使用`rectificationSuggestion/suggestion`作为展示内容；图片字段兼容`rectificationImages/rectifyImages/rectificationImageUrls`。
- 文件：`admin/src/views/inspection/monthly/components/ReviewDialog.vue`

### Web端：仅保留“整改效果/复查效果”，使用建议字段（2025-11-17）
- 诉求：编辑弹框与复查弹框仅保留两个文本字段“整改效果”“复查效果”，分别使用后端字段`rectificationSuggestion`与`reviewSuggestion`。
- 变更：
  - 整改确认弹框：移除图片上传，仅保留“整改效果”（提交`rectificationSuggestion`）。
  - 复查确认弹框：仅保留“复查效果”（提交`reviewSuggestion`）。
  - 查看弹框：展示“整改效果”与“复查效果”，分别兼容显示`rectificationEffect/rectificationSuggestion`与`reviewEffect/reviewSuggestion`。
- 文件：
  - `admin/src/views/inspection/monthly/components/RectificationConfirmDialog.vue`
  - `admin/src/views/inspection/monthly/components/ReviewDialog.vue`
  - `admin/src/views/inspection/monthly/components/ViewDialog.vue`
