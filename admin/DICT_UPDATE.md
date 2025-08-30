# 字典管理功能更新说明

## 更新内容

### 1. 后端接口统一
- 将字典管理相关接口从 `/dict` 统一更改为 `/dictionary`
- 字段名称与后端实体类保持一致：
  - `dictId` → `id`
  - `dictSort` → `sortOrder`
  - `cssClass` → `description`
  - `remark` → `description`

### 2. 前端API更新
- 更新 `src/api/system.js` 中的 `dictApi`
- 新增接口方法：
  - `getByType(dictType)` - 按类型查询字典数据
  - `getAllData()` - 获取所有字典数据
  - `getAllTypes()` - 获取所有字典类型
  - `checkLabel()` - 检查字典标签是否存在
  - `checkValue()` - 检查字典键值是否存在

### 3. 字典管理页面更新
- 更新 `src/views/system/dict/index.vue`
- 修正字段名称映射
- 添加表单验证（字典标签和键值唯一性检查）
- 移除状态切换功能（后端暂未提供相关接口）
- 修正分页参数名称

### 4. 通用字典选择组件
- 新增 `src/components/DictSelect.vue` 组件
- 支持按字典类型动态加载选项
- 支持单选/多选模式
- 支持搜索过滤

### 5. 仪表盘统计更新
- 更新 `src/views/dashboard/index.vue`
- 使用真实API获取统计数据
- 并行加载提升性能

## 使用说明

### 字典选择组件使用示例

```vue
<template>
  <!-- 基础用法 -->
  <DictSelect v-model="selectedValue" dict-type="profession" />
  
  <!-- 多选模式 -->
  <DictSelect 
    v-model="selectedValues" 
    dict-type="location" 
    :multiple="true" 
    placeholder="请选择部位"
  />
  
  <!-- 支持搜索 -->
  <DictSelect 
    v-model="selectedValue" 
    dict-type="status" 
    :filterable="true"
  />
</template>

<script setup>
import { ref } from 'vue'
import DictSelect from '@/components/DictSelect.vue'

const selectedValue = ref('')
const selectedValues = ref([])
</script>
```

### 字典API使用示例

```javascript
import { dictApi } from '@/api/system'

// 获取专业字典数据
const professions = await dictApi.getByType('profession')

// 获取所有字典类型
const types = await dictApi.getAllTypes()

// 检查字典标签是否存在
const labelExists = await dictApi.checkLabel('profession', '软件工程')
```

## 注意事项

1. 确保后端字典管理接口已部署并可正常访问
2. 字典类型需要在后端预先配置
3. 使用字典选择组件时，确保传入正确的 `dict-type` 值
4. 表单验证会实时检查字典标签和键值的唯一性

## 兼容性

- Vue 3.x
- Element Plus 2.x
- 支持现代浏览器