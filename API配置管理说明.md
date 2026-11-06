# API配置管理说明

## 概述
本文档说明了榆林公司岗检系统中API配置的统一管理方案，解决了之前硬编码IP地址导致的配置不一致问题。

## 问题背景
之前系统中存在以下问题：
1. 多个文件中硬编码了不同的IP地址（192.168.75.128、192.168.0.35等）
2. URL拼接中存在空格问题（如：`http:// 192.168.75.128:8081`）
3. 开发环境和生产环境配置混乱
4. 前端管理系统和移动端配置不统一

## 解决方案

### 1. 前端管理系统（admin）

#### 环境配置文件
- **开发环境**: `.env.development`
  ```
  # 开发环境配置
  VITE_API_BASE_URL=http://192.168.0.35:8081
  ```

- **生产环境**: `.env.production`
  ```
  # 生产环境配置
  VITE_API_BASE_URL=http://192.168.0.35:8081
  ```

#### API配置文件
- **位置**: `src/config/api.js`
- **功能**: 
  - 开发环境使用Vite代理（相对路径）
  - 生产环境使用环境变量配置
  - 提供统一的URL拼接函数

#### Vite配置
- **位置**: `vite.config.js`
- **功能**: 
  - 从环境变量读取代理目标地址
  - 支持开发环境热重载

### 2. 移动端应用（gyxj）

#### API配置文件
- **位置**: `config/api.uts`
- **功能**: 
  - 根据环境自动选择API地址
  - 支持开发和生产环境配置

#### 网络安全配置
- **位置**: `manifest.json`
- **功能**: 
  - 配置允许的HTTP域名
  - 支持多个服务器地址

### 3. 统一的配置函数

#### 前端管理系统
```javascript
// 获取API基础URL
export const getApiBaseUrl = () => {
  if (import.meta.env.DEV) {
    return '' // 开发环境使用代理
  }
  return import.meta.env.VITE_API_BASE_URL || 'http://192.168.75.128:8081'
}

// 获取完整API URL
export const getFullApiUrl = (endpoint) => {
  const baseUrl = getApiBaseUrl()
  return `${baseUrl}/api${endpoint}`
}

// 解析图片URL
export const parseImageUrls = (imageUrls) => {
  // 统一的图片URL解析逻辑
}
```

#### 移动端应用
```typescript
// 获取API基础URL
const getBaseUrl = (): string => {
  if (process.env.NODE_ENV === 'development') {
    return 'http://192.168.75.128:8081'
  }
  return 'http://192.168.75.128:8081'
}

// 获取完整的API URL
export const getApiUrl = (endpoint: string): string => {
  return `${apiConfig.baseUrl}/api${endpoint}`
}
```

## 配置修改指南

### 更改服务器地址
1. **前端管理系统**: 修改 `.env.development` 和 `.env.production` 文件中的 `VITE_API_BASE_URL`
2. **移动端应用**: 修改 `config/api.uts` 文件中的 `getBaseUrl` 函数
3. **移动端网络配置**: 修改 `manifest.json` 文件中的 `domain-config`

### 添加新环境
1. 创建对应的环境配置文件（如 `.env.staging`）
2. 在API配置文件中添加相应的环境判断逻辑

## 已修复的文件列表

### 前端管理系统
- `src/config/api.js` - 新建统一API配置文件
- `src/views/inspection/weekly/components/ViewDialog.vue` - 移除硬编码，使用统一配置
- `src/views/inspection/weekly/components/ReviewDialog.vue` - 移除硬编码，使用统一配置
- `src/views/inspection/weekly/components/AddDialog.vue` - 移除硬编码，使用统一配置
- `src/views/inspection/weekly/components/RectificationDialog.vue` - 移除硬编码，使用统一配置
- `vite.config.js` - 使用环境变量配置代理

### 移动端应用
- `config/api.uts` - 修复硬编码IP和空格问题
- `manifest.json` - 修复网络安全配置中的硬编码IP

## 注意事项
1. 修改配置后需要重启开发服务器
2. 生产环境部署时确保环境变量正确设置
3. 移动端应用需要重新编译打包
4. 确保所有服务器地址在网络安全配置中都有配置

## 测试建议
1. 测试开发环境下的API调用
2. 测试生产环境下的API调用
3. 测试图片URL的正确显示
4. 测试文件上传功能
5. 测试移动端的网络连接

## 维护建议
1. 定期检查是否有新的硬编码IP地址
2. 保持环境配置文件的同步
3. 文档化任何配置变更
4. 建立配置变更的审核流程