<template>
  <div class="weekly-submit-container">
    <!-- 查询条件区域 -->
    <div class="search-section">
      <el-card>
        <el-form :model="searchForm" inline>
          <el-form-item label="责任人">
            <el-input
              v-model="searchForm.responsiblePerson"
              placeholder="请输入责任人"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="检查人">
            <el-input
              v-model="searchForm.inspector"
              placeholder="请输入检查人"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="所属部门">
            <el-select
              v-model="searchForm.departmentId"
              placeholder="请选择部门"
              clearable
              style="width: 200px"
              :disabled="!isAdminUser"
            >
              <el-option
                v-for="dept in departmentList"
                :key="dept.id"
                :label="dept.deptName"
                :value="dept.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="记录时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <el-button
        type="primary"
        :disabled="selectedRows.length === 0"
        @click="handleBatchSubmit"
      >
        <el-icon><Upload /></el-icon>
        批量提交
      </el-button>
      <span class="selected-info" v-if="selectedRows.length > 0">
        已选择 {{ selectedRows.length }} 条记录
      </span>
    </div>

    <!-- 数据列表 -->
    <div class="table-section">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="responsiblePerson" label="责任人" width="120" />
        <el-table-column prop="problemDescription" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="location" label="位置" width="150" show-overflow-tooltip />
        <el-table-column prop="inspector" label="检查人" width="120" />
        <el-table-column label="所属部门" width="150">
          <template #default="{ row }">
            {{ getDepartmentName(row.departmentId) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="记录时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
             <el-button 
               type="primary" 
               size="small" 
               @click="handleEdit(row)"
             >
               编辑
             </el-button>
             <el-button 
               type="danger" 
               size="small" 
               @click="handleDelete(row)"
               v-if="hasSubmitPermission()"
             >
               删除
             </el-button>
             <el-button type="success" size="small" @click="handleSubmit(row)">
               提交
             </el-button>
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

    <!-- 编辑对话框 -->
    <EditDialog
      v-model="editDialog.visible"
      :record="editDialog.record"
      @success="handleEditSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Upload } from '@element-plus/icons-vue'
import EditDialog from './components/EditDialog.vue'
import { getSubmitRecords, updateRecordStatus, deleteRecord } from '@/api/inspection'
import { useUserStore } from '@/stores/user'
import { hasSubmitPermission, isWeeklyAdmin } from '@/utils/permission'

// 搜索表单
const searchForm = reactive({
  responsiblePerson: '',
  inspector: '',
  departmentId: '',
  dateRange: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 编辑对话框
const editDialog = reactive({
  visible: false,
  record: null
})

// 部门数据
const departmentList = ref([])

// 用户权限
const isAdminUser = ref(false)

// 获取部门名称
const getDepartmentName = (departmentId) => {
  if (!departmentId) return '-'
  const dept = departmentList.value.find(item => item.id === departmentId)
  return dept ? dept.deptName : departmentId
}

// 加载部门数据
const loadDepartmentList = async () => {
  try {
    const { getDeptList } = await import('@/api/inspection')
    const response = await getDeptList()
    if (response.code === 200) {
      departmentList.value = response.data || []
    }
  } catch (error) {
    console.error('加载部门数据失败:', error)
  }
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      status: 4, // 查询状态为4的记录
      responsiblePersonId: searchForm.responsiblePerson || undefined,
      inspectorId: searchForm.inspector || undefined,
      departmentId: searchForm.departmentId || undefined,
      startTime: searchForm.dateRange?.[0] || undefined,
      endTime: searchForm.dateRange?.[1] || undefined
    }
    
    const response = await getSubmitRecords(params)
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
  fetchData()
}

// 重置
const handleReset = () => {
  searchForm.responsiblePerson = ''
  searchForm.inspector = ''
  searchForm.departmentId = ''
  searchForm.dateRange = null
  // 非管理员用户重置后重新设置部门
  if (!isAdminUser.value) {
    getCurrentUserDepartment()
  }
  pagination.current = 1
  fetchData()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 批量提交
const handleBatchSubmit = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要提交的记录')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要提交选中的 ${selectedRows.value.length} 条记录吗？提交后状态将变更为待整改。`,
      '批量提交确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const ids = selectedRows.value.map(row => row.id)
    const response = await updateRecordStatus({ ids, status: 0 })
    
    if (response.code === 200) {
      ElMessage.success('批量提交成功')
      selectedRows.value = []
      fetchData()
    } else {
      ElMessage.error(response.message || '批量提交失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量提交失败:', error)
      ElMessage.error('批量提交失败')
    }
  }
}

// 编辑
const handleEdit = (row) => {
  Object.assign(editDialog, {
    record: { ...row },
    visible: true
  })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除责任人为"${row.responsiblePerson}"的记录吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteRecord(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
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

// 单个提交
const handleSubmit = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要提交责任人为"${row.responsiblePerson}"的记录吗？提交后状态将变更为待整改。`,
      '提交确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await updateRecordStatus({ ids: [row.id], status: 0 })
    if (response.code === 200) {
      ElMessage.success('提交成功')
      fetchData()
    } else {
      ElMessage.error(response.message || '提交失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  }
}


// 编辑成功回调
const handleEditSuccess = () => {
  editDialog.visible = false
  fetchData()
}

// 分页变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  fetchData()
}

// 获取当前用户部门信息
const getCurrentUserDepartment = () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    // 判断是否为周检管理员（周检提交页面只认周检权限，不再用已废弃的 isAdmin 字段）
    isAdminUser.value = isWeeklyAdmin()
    
    // 非周检管理员用户自动设置部门
    if (!isAdminUser.value && userInfo.deptId) {
      searchForm.departmentId = userInfo.deptId
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 初始化
onMounted(() => {
  getCurrentUserDepartment()
  loadDepartmentList()
  fetchData()
})
</script>

<style scoped>
.weekly-submit-container {
  padding: 20px;
}

.search-section {
  margin-bottom: 20px;
}

.action-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.selected-info {
  color: #409eff;
  font-size: 14px;
}

.table-section {
  background: white;
  border-radius: 4px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>