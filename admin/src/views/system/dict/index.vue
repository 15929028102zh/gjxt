<template>
  <div class="dict-management">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">字典管理</h1>
      </div>
      
      <!-- 搜索和操作栏 -->
      <div class="toolbar">
        <div class="search-form">
          <el-input
            v-model="searchForm.dictType"
            placeholder="请输入字典类型"
            style="width: 200px"
            clearable
          />
          <el-input
            v-model="searchForm.dictLabel"
            placeholder="请输入字典标签"
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
            新增字典
          </el-button>
          <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button type="success" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </div>
      </div>
      
      <!-- 字典表格 -->
      <div class="table-container">
        <el-table
          v-loading="loading"
          :data="tableData"
          @selection-change="handleSelectionChange"
          stripe
          border
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="字典ID" width="100" />
          <el-table-column prop="dictType" label="字典类型" width="150">
            <template #default="{ row }">
              <el-tag type="info">{{ row.dictType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="dictLabel" label="字典标签" width="120" />
          <el-table-column prop="dictValue" label="字典值" width="120" />
          <el-table-column prop="sortOrder" label="排序" width="80" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="字典描述" min-width="150" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <!-- 字典表单对话框 -->
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
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="字典类型" prop="dictType">
              <el-input v-model="form.dictType" placeholder="请输入字典类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字典标签" prop="dictLabel">
              <el-input v-model="form.dictLabel" placeholder="请输入字典标签" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="字典值" prop="dictValue">
              <el-input v-model="form.dictValue" placeholder="请输入字典值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字典编码">
              <el-input v-model="form.dictCode" placeholder="请输入字典编码" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="字典描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入字典描述"
          />
        </el-form-item>
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
import { dictApi } from '@/api/system'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const selectedIds = ref([])

// 搜索表单
const searchForm = reactive({
  dictType: '',
  dictLabel: '',
  status: ''
})

// 分页数据
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 表单数据
const form = reactive({
  id: null,
  dictType: '',
  dictLabel: '',
  dictValue: '',
  dictCode: '',
  sortOrder: 0,
  status: 1,
  description: ''
})

// 表单验证规则
const formRules = {
  dictType: [
    { required: true, message: '请输入字典类型', trigger: 'blur' }
  ],
  dictLabel: [
    { required: true, message: '请输入字典标签', trigger: 'blur' },
    {
      validator: async (rule, value, callback) => {
        if (value && form.dictType) {
          try {
            const response = await dictApi.checkLabel(form.dictType, value, form.id)
            if (response.data) {
              callback(new Error('字典标签已存在'))
            } else {
              callback()
            }
          } catch (error) {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  dictValue: [
    { required: true, message: '请输入字典值', trigger: 'blur' },
    {
      validator: async (rule, value, callback) => {
        if (value && form.dictType) {
          try {
            const response = await dictApi.checkValue(form.dictType, value, form.id)
            if (response.data) {
              callback(new Error('字典键值已存在'))
            } else {
              callback()
            }
          } catch (error) {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return form.id ? '编辑字典' : '新增字典'
})

// 加载字典列表
const loadDictList = async () => {
  loading.value = true
  
  try {
    const params = {
      current: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    
    const response = await dictApi.getPage(params)
    
    if (response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || 0
    }
  } catch (error) {
    console.error('加载字典列表失败:', error)
    ElMessage.error('加载字典列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadDictList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    dictType: '',
    dictLabel: '',
    status: ''
  })
  handleSearch()
}

// 新增字典
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑字典
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    dictType: row.dictType,
    dictLabel: row.dictLabel,
    dictValue: row.dictValue,
    dictCode: row.dictCode,
    sortOrder: row.sortOrder,
    status: row.status,
    description: row.description
  })
  dialogVisible.value = true
}

// 删除字典
const handleDelete = async (row) => {
  console.log('删除按钮被点击，行数据:', row)
  try {
    await ElMessageBox.confirm(
      `确定要删除字典 "${row.dictLabel}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    console.log('用户确认删除，准备调用API，ID:', row.id)
    const result = await dictApi.delete(row.id)
    console.log('删除API调用结果:', result)
    ElMessage.success('删除成功')
    loadDictList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除字典失败:', error)
      ElMessage.error('删除失败：' + (error.message || error))
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个字典吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await dictApi.batchDelete(selectedIds.value)
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    loadDictList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除字典失败:', error)
    }
  }
}



// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  loadDictList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.page = page
  loadDictList()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await dictApi.update(form)
      ElMessage.success('更新成功')
    } else {
      await dictApi.create(form)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadDictList()
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
    dictType: '',
    dictLabel: '',
    dictValue: '',
    dictCode: '',
    sortOrder: 0,
    status: 1,
    description: ''
  })
  
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadDictList()
})
</script>

<style scoped>
.dict-management {
  padding: 0;
}
</style>