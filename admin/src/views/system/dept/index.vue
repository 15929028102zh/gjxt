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
          :data="tableData"
          stripe
          border
        >
          <el-table-column prop="deptName" label="部门名称" width="200">
            <template #default="{ row }">
              <span>{{ row.deptName }}</span>
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
import { deptApi } from '@/api/system'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  deptName: '',
  status: ''
})

// 表格数据
const tableData = ref([])

// 表单数据
const form = reactive({
  id: null,
  deptName: '',
  description: '',
  sortOrder: 0,
  status: 1
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
    const params = {
      ...searchForm
    }
    
    const response = await deptApi.getPage(params)
    
    if (response.data) {
      tableData.value = response.data.records || response.data
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
    ElMessage.error('加载部门列表失败')
  } finally {
    loading.value = false
  }
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
  dialogVisible.value = true
}



// 编辑部门
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    deptName: row.deptName,
    description: row.description,
    sortOrder: row.sortOrder,
    status: row.status
  })
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
    
    if (form.id) {
      await deptApi.update(form)
      ElMessage.success('更新成功')
    } else {
      await deptApi.create(form)
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

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    deptName: '',
    description: '',
    sortOrder: 0,
    status: 1
  })
  
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
</style>