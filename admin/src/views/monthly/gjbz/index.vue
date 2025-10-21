<template>
  <div class="gjbz-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>岗检标准管理</h2>
      <p>管理岗检标准数据</p>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="类别">
          <el-input v-model="searchForm.type" placeholder="请输入类别" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="模块">
          <el-input v-model="searchForm.moudle" placeholder="请输入模块" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="责任要素">
          <el-input v-model="searchForm.responsibility" placeholder="请输入责任要素" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="operation-buttons">
        <el-button type="primary" @click="handleAdd" :icon="Plus" v-if="hasMonthlyPermission()">新增</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0" :icon="Delete" v-if="hasMonthlyPermission()">批量删除</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="type" label="专业类别" width="120" />
        <el-table-column prop="moudle" label="基本模块" width="150" />
        <el-table-column prop="responsibility" label="责任要素" width="200" />
        <el-table-column prop="issue" label="检查内容" width="200" />
        <el-table-column prop="quality" label="问题性质" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)" :icon="Edit" v-if="hasMonthlyPermission()">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)" :icon="Delete" v-if="hasMonthlyPermission()">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="类型" prop="type">
          <el-input v-model="formData.type" placeholder="请输入类型" />
        </el-form-item>
        <el-form-item label="模块" prop="moudle">
          <el-input v-model="formData.moudle" placeholder="请输入模块" />
        </el-form-item>
        <el-form-item label="责任单位" prop="responsibility">
          <el-input v-model="formData.responsibility" placeholder="请输入责任单位" />
        </el-form-item>
        <el-form-item label="问题类型" prop="problemType">
          <el-input v-model="formData.problemType" placeholder="请输入问题类型" />
        </el-form-item>
        <el-form-item label="质量等级" prop="qualityLevel">
          <el-input v-model="formData.qualityLevel" placeholder="请输入质量等级" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Edit } from '@element-plus/icons-vue'
import { gjbzDataApi } from '@/api/gjbz'
import { hasMonthlyPermission } from '@/utils/permission'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const selectedRows = ref([])
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  type: '',
  moudle: '',
  responsibility: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 表单数据
const formData = reactive({
  id: '',
  type: '',
  moudle: '',
  responsibility: '',
  problemType: '',
  qualityLevel: ''
})

// 表单验证规则
const formRules = {
  type: [{ required: true, message: '请输入类型', trigger: 'blur' }],
  moudle: [{ required: true, message: '请输入模块', trigger: 'blur' }],
  responsibility: [{ required: true, message: '请输入责任单位', trigger: 'blur' }],
  problemType: [{ required: true, message: '请输入问题类型', trigger: 'blur' }],
  qualityLevel: [{ required: true, message: '请输入质量等级', trigger: 'blur' }]
}

// 获取数据列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    }
    const response = await gjbzDataApi.getPage(params)
    if (response.code === 200) {
      tableData.value = response.data.records || []
      pagination.total = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getList()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    type: '',
    moudle: '',
    responsibility: ''
  })
  pagination.current = 1
  getList()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增岗检标准'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑岗检标准'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await gjbzDataApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    const response = await gjbzDataApi.batchDelete(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      selectedRows.value = []
      getList()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  getList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getList()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    let response
    if (isEdit.value) {
      response = await gjbzDataApi.update(formData.id, formData)
    } else {
      response = await gjbzDataApi.add(formData)
    }
    
    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '新增失败'))
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: '',
    type: '',
    moudle: '',
    responsibility: '',
    problemType: '',
    qualityLevel: ''
  })
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.gjbz-container {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-form {
  margin-bottom: 16px;
}

.operation-buttons {
  display: flex;
  gap: 12px;
}

.table-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table__header) {
  background-color: #f8f9fa;
}

:deep(.el-table th) {
  background-color: #f8f9fa !important;
  color: #303133;
  font-weight: 600;
}

:deep(.el-button) {
  border-radius: 6px;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  padding: 20px 20px 10px;
}

:deep(.el-dialog__body) {
  padding: 10px 20px 20px;
}
</style>