<template>
  <div class="dept-management">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">部门管理</h1>
      </div>
      
      <!-- 搜索和操作栏 -->
      <div class="toolbar">
        <div class="search-form">
          <el-input
            v-model="searchForm.deptName"
            placeholder="请输入部门名称"
            style="width: 200px"
            clearable
          />
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            style="width: 120px"
            clearable
          >
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
        
        <div class="action-buttons">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增部门
          </el-button>
        </div>
      </div>
      
      <!-- 部门表格 -->
      <div class="table-container">
        <el-table
          v-loading="loading"
          :data="deptList"
          stripe
          border
          row-key="id"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          default-expand-all
        >
          <el-table-column prop="deptName" label="部门名称" min-width="200">
            <template #default="{ row, $index }">
              <div class="dept-name-cell">
                <span class="dept-level-indicator" :style="{ paddingLeft: getDeptLevel(row) * 20 + 'px' }">
                  <el-icon v-if="row.children && row.children.length > 0" class="dept-folder-icon">
                    <Folder />
                  </el-icon>
                  <el-icon v-else class="dept-file-icon">
                    <Document />
                  </el-icon>
                </span>
                <span class="dept-name-text">{{ row.deptName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="部门描述" width="200" />
          <el-table-column prop="sortOrder" label="排序" width="80" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    
    <!-- 部门表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="部门级别" prop="deptLevel">
          <el-select v-model="form.deptLevel" placeholder="请选择部门级别" style="width: 100%" @change="handleDeptLevelChange">
            <el-option label="1级部门" :value="1" />
            <el-option label="2级部门" :value="2" />
            <el-option label="3级部门" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="父部门" prop="parentId" v-show="form.deptLevel > 1">
          <el-tree-select
            v-model="form.parentId"
            :data="parentDeptOptions"
            :props="{ value: 'id', label: 'deptName', children: 'children' }"
            placeholder="请选择父部门（不选择则为顶级部门）"
            clearable
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        
        <el-form-item label="部门描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入部门描述"
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Folder, Document } from '@element-plus/icons-vue'
import { deptApi } from '@/api/system'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const parentDeptOptions = ref([])

// 搜索表单
const searchForm = reactive({
  deptName: '',
  status: ''
})

// 表格数据
const tableData = ref([])
const deptList = ref([])

// 表单数据
const form = reactive({
  id: null,
  parentId: '0',
  deptName: '',
  description: '',
  sortOrder: 0,
  status: 1,
  deptLevel: 1 // 新增部门级别字段，默认为1级
})

// 表单验证规则
const formRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return form.id ? '编辑部门' : '新增部门'
})

// 加载部门列表
const loadDeptList = async () => {
  loading.value = true
  
  try {
    // 使用树形接口获取所有部门数据
    const response = await deptApi.getTree()
    let allDepts = response.data || []
    
    // 根据搜索条件过滤数据
    if (searchForm.deptName || searchForm.status !== undefined) {
      allDepts = filterDeptTree(allDepts, searchForm)
    }
    
    deptList.value = allDepts
     // 树形结构不需要分页
     pagination.value.total = countAllDepts(allDepts)
   } catch (error) {
     console.error('加载部门列表失败:', error)
   } finally {
     loading.value = false
   }
 }

// 过滤部门树形数据
 const filterDeptTree = (depts, filters) => {
   return depts.filter(dept => {
     let match = true
     
     if (filters.deptName && !dept.deptName.includes(filters.deptName)) {
       match = false
     }
     
     if (filters.status !== '' && dept.status !== parseInt(filters.status)) {
       match = false
     }
     
     // 递归过滤子部门
     if (dept.children && dept.children.length > 0) {
       dept.children = filterDeptTree(dept.children, filters)
       // 如果子部门有匹配的，父部门也应该显示
       if (dept.children.length > 0) {
         match = true
       }
     }
     
     return match
   })
 }
 
 // 计算部门总数（包括子部门）
 const countAllDepts = (depts) => {
   let count = 0
   depts.forEach(dept => {
     count++
     if (dept.children && dept.children.length > 0) {
       count += countAllDepts(dept.children)
     }
   })
   return count
 }
 
 // 计算部门层级（用于缩进显示）
 const getDeptLevel = (dept) => {
   // 由于使用了tree-props，Element Plus会自动处理层级
   // 这里返回0，让Element Plus自己处理缩进
   return 0
 }



// 搜索
const handleSearch = () => {
  loadDeptList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    deptName: '',
    status: ''
  })
  handleSearch()
}



// 新增部门
const handleAdd = () => {
  resetForm()
  // 新增时默认为1级部门，不需要加载父部门选项
  dialogVisible.value = true
}



// 编辑部门
const handleEdit = async (row) => {
  // 计算当前部门的级别
  const currentLevel = calculateDeptLevel(row.id, deptList.value)
  
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId,
    deptName: row.deptName,
    description: row.description,
    sortOrder: row.sortOrder,
    status: row.status,
    deptLevel: currentLevel
  })
  
  // 根据部门级别加载对应的父部门选项
  if (currentLevel === 1) {
    parentDeptOptions.value = []
  } else if (currentLevel === 2) {
    await loadParentDeptsByLevel(1)
  } else if (currentLevel === 3) {
    await loadParentDeptsByLevel(2)
  }
  
  dialogVisible.value = true
}

// 删除部门
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除部门 "${row.deptName}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deptApi.delete(row.id)
    ElMessage.success('删除成功')
    loadDeptList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除部门失败:', error)
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    // 准备提交数据
    const submitData = { ...form }
    
    // 根据部门级别设置parentId
    if (submitData.deptLevel === 1) {
      // 1级部门的parentId固定为'0'
      submitData.parentId = '0'
    } else if (!submitData.parentId || submitData.parentId === '') {
      // 其他级别如果没有选择父部门，设置为'0'
      submitData.parentId = '0'
    }
    
    if (form.id) {
      await deptApi.update(submitData)
      ElMessage.success('更新成功')
    } else {
      await deptApi.create(submitData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadDeptList()
  } catch (error) {
    console.error('提交表单失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 初始化父部门选项
const initParentDeptOptions = async (excludeId = null) => {
  try {
    const response = await deptApi.getDeptTree()
    let deptTree = response.data || []
    
    // 如果是编辑模式，需要排除当前部门及其子部门
    if (excludeId) {
      deptTree = filterExcludeDept(deptTree, excludeId)
    }
    
    // 添加顶级部门选项
    parentDeptOptions.value = [
      { id: '0', deptName: '顶级部门', children: deptTree }
    ]
  } catch (error) {
    console.error('加载父部门选项失败:', error)
  }
}

// 过滤排除指定部门及其子部门
const filterExcludeDept = (depts, excludeId) => {
  return depts.filter(dept => {
    if (dept.id === excludeId) {
      return false
    }
    if (dept.children && dept.children.length > 0) {
      dept.children = filterExcludeDept(dept.children, excludeId)
    }
    return true
  })
}

// 处理部门级别变化
const handleDeptLevelChange = async (level) => {
  // 重置父部门选择
  form.parentId = '0'
  
  if (level === 1) {
    // 1级部门不需要父部门
    parentDeptOptions.value = []
  } else if (level === 2) {
    // 2级部门，加载所有1级部门
    await loadParentDeptsByLevel(1)
  } else if (level === 3) {
    // 3级部门，加载所有2级部门
    await loadParentDeptsByLevel(2)
  }
}

// 根据级别加载父部门选项
const loadParentDeptsByLevel = async (targetLevel) => {
  try {
    const response = await deptApi.getTree()
    let deptTree = response.data || []
    
    // 过滤出指定级别的部门
    const filteredDepts = filterDeptsByLevel(deptTree, targetLevel, 1)
    
    parentDeptOptions.value = filteredDepts
  } catch (error) {
    console.error('加载父部门选项失败:', error)
  }
}

// 递归过滤指定级别的部门
const filterDeptsByLevel = (depts, targetLevel, currentLevel) => {
  const result = []
  
  depts.forEach(dept => {
    if (currentLevel === targetLevel) {
      // 找到目标级别的部门，不包含子部门
      result.push({
        id: dept.id,
        deptName: dept.deptName,
        children: []
      })
    } else if (currentLevel < targetLevel && dept.children && dept.children.length > 0) {
      // 继续向下查找
      const childResults = filterDeptsByLevel(dept.children, targetLevel, currentLevel + 1)
      result.push(...childResults)
    }
  })
  
  return result
}

// 计算部门级别
const calculateDeptLevel = (deptId, deptTree, currentLevel = 1) => {
  for (const dept of deptTree) {
    if (dept.id === deptId) {
      return currentLevel
    }
    if (dept.children && dept.children.length > 0) {
      const level = calculateDeptLevel(deptId, dept.children, currentLevel + 1)
      if (level > 0) {
        return level
      }
    }
  }
  return 1 // 默认为1级
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    parentId: '0',
    deptName: '',
    description: '',
    sortOrder: 0,
    status: 1,
    deptLevel: 1
  })
  
  // 重置父部门选项
  parentDeptOptions.value = []
  
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadDeptList()
})
</script>

<style scoped>
.dept-management {
  padding: 0;
}

.dept-name-cell {
  display: flex;
  align-items: center;
}

.dept-level-indicator {
  display: flex;
  align-items: center;
  margin-right: 8px;
}

.dept-folder-icon {
  color: #409eff;
  font-size: 16px;
}

.dept-file-icon {
  color: #909399;
  font-size: 16px;
}

.dept-name-text {
  font-weight: 500;
}
</style>