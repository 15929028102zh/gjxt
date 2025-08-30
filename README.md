# 石油工业园区设备巡检系统

## 项目简介

本系统是一个基于Spring Boot + MyBatis + MySQL + Vue + ElementUI的设备巡检管理系统，同时提供uniapp移动端应用。

## 技术架构

### 后端技术栈
- Spring Boot 2.x
- MyBatis
- MySQL 8.0
- Maven
- Java 1.8

### 前端技术栈
- Vue 3.x
- Element Plus
- Vue Router (History模式)
- Axios
- ECharts

### 移动端技术栈
- uniapp
- 适配安卓端

## 项目结构

```
gjxt/
├── backend/                 # 后端Spring Boot项目
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── pom.xml
├── frontend/                # 前端Vue管理系统
│   ├── src/
│   ├── public/
│   └── package.json
├── mobile/                  # 移动端uniapp项目
│   ├── pages/
│   ├── components/
│   └── manifest.json
└── docs/                    # 文档
    └── database.sql
```

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