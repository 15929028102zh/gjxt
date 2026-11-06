<template>
  <div class="user-management">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">用户管理</h1>
      </div>
      
      <!-- 搜索和操作栏 -->
      <div class="toolbar">
        <div class="search-form">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            style="width: 200px"
            clearable
          />
          <el-select
            v-model="searchForm.deptId"
            placeholder="请选择部门"
            style="width: 150px"
            clearable
          >
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
            />
          </el-select>
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
            新增用户
          </el-button>
        </div>
      </div>
      
      <!-- 用户表格 -->
      <div class="table-container">
        <el-table
          v-loading="loading"
          :data="tableData"
          @selection-change="handleSelectionChange"
          stripe
          border
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="nickname" label="昵称" width="120" />
          <el-table-column prop="email" label="邮箱" width="180" />
          <el-table-column prop="phone" label="手机号" width="130" />
          <el-table-column prop="deptName" label="部门" width="120" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="250" fixed="right">
            <template #default="{ row }">
              <el-button 
                v-if="canEditUser()" 
                type="primary" 
                size="small" 
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button 
                v-if="canManageUserStatus()"
                :type="row.status === 1 ? 'warning' : 'success'" 
                size="small" 
                @click="handleToggleStatus(row)"
              >
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button 
                v-if="canResetPassword()"
                type="warning" 
                size="small" 
                @click="handleResetPassword(row)"
              >
                重置密码
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <!-- 用户表单对话框 -->
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input 
                v-model="form.username" 
                placeholder="请输入用户名（仅支持数字和字母）" 
                @input="handleUsernameInput"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择部门" style="width: 100%">
                <el-option
                  v-for="dept in deptOptions"
                  :key="dept.id"
                  :label="dept.deptName"
                  :value="dept.id"
                />
              </el-select>
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
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="周检管理员">
              <el-checkbox
                :model-value="!!form.isWeeklyAdmin"
                @update:model-value="(val) => (form.isWeeklyAdmin = val ? 1 : 0)"
              >是</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月检管理员">
              <el-checkbox
                :model-value="!!form.isMonthlyAdmin"
                @update:model-value="(val) => (form.isMonthlyAdmin = val ? 1 : 0)"
              >是</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="周检权限" prop="weeklyPermission">
              <el-radio-group v-model="form.weeklyPermission">
                <el-radio :label="0">允许编辑删除</el-radio>
                <el-radio :label="1">禁用编辑删除</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月检权限" prop="monthlyPermission">
              <el-radio-group v-model="form.monthlyPermission">
                <el-radio :label="0">允许编辑删除</el-radio>
                <el-radio :label="1">禁用编辑删除</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="月检审核权限" prop="auditPermission">
              <el-radio-group v-model="form.auditPermission">
                <el-radio :label="1">可审核月检</el-radio>
                <el-radio :label="0">不可审核</el-radio>
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
import { userApi, deptApi } from '@/api/system'
import { isAdmin, canEditUser, canDeleteUser, canManageUserStatus, canResetPassword } from '@/utils/permission'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const selectedIds = ref([])

// 搜索表单
const searchForm = reactive({
  username: '',
  deptId: null,
  status: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 部门选项
const deptOptions = ref([])

// 表单数据
const form = reactive({
  id: null,
  username: '',
  nickname: '',
  email: '',
  phone: '',
  deptId: null,
  department: '',
  status: 1,
  isAdmin: 0,
  isWeeklyAdmin: 0,
  isMonthlyAdmin: 0,
  weeklyPermission: 0,
  monthlyPermission: 0,
  auditPermission: 0
})

// 表单验证规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]+$/, message: '用户名只能包含数字和字母', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  deptId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return form.id ? '编辑用户' : '新增用户'
})

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      username: searchForm.username,
      deptId: searchForm.deptId,
      status: searchForm.status
    }
    
    const response = await userApi.getPage(params)
    
    if (response.data) {
      tableData.value = response.data.records || []
      pagination.total = response.data.total || 0
    }
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadUserList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    username: '',
    deptId: null,
    status: ''
  })
  handleSearch()
}

// 新增用户
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    username: row.username,
    nickname: row.nickname,
    email: row.email,
    phone: row.phone,
    deptId: row.deptId,
    department: row.deptName || '',
    status: row.status,
    isAdmin: row.isAdmin || 0,
    isWeeklyAdmin: row.isWeeklyAdmin || 0,
    isMonthlyAdmin: row.isMonthlyAdmin || 0,
    weeklyPermission: row.weeklyPermission || 0,
    monthlyPermission: row.monthlyPermission || 0,
    auditPermission: row.auditPermission || 0
  })
  dialogVisible.value = true
}

// 切换用户状态
const handleToggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const statusText = newStatus === 1 ? '启用' : '禁用'
  
  try {
    await ElMessageBox.confirm(
      `确定要${statusText}用户 "${row.username}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await userApi.updateStatus(row.id, newStatus)
    ElMessage.success(`${statusText}成功`)
    loadUserList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新用户状态失败:', error)
    }
  }
}

// 重置密码
const handleResetPassword = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${row.username}" 的密码吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await userApi.resetPassword(row.id)
    ElMessage.success('密码重置成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重置密码失败:', error)
    }
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  loadUserList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.current = page
  loadUserList()
}

// 用户名输入限制
const handleUsernameInput = (value) => {
  // 只允许数字和字母
  const filteredValue = value.replace(/[^a-zA-Z0-9]/g, '')
  form.username = filteredValue
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const submitData = { ...form }
    // is_admin 字段已废弃,前端不再写
    delete submitData.isAdmin

    // 根据选择的部门ID设置部门名称
    if (submitData.deptId) {
      const selectedDept = deptOptions.value.find(dept => dept.id === submitData.deptId)
      if (selectedDept) {
        submitData.department = selectedDept.deptName
      }
    } else {
      submitData.department = ''
    }
    
    if (form.id) {
      await userApi.update(submitData)
      ElMessage.success('更新成功')
    } else {
      // 新增用户时，密码默认为用户名
      submitData.password = form.username
      await userApi.create(submitData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadUserList()
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
    username: '',
    nickname: '',
    email: '',
    phone: '',
    deptId: null,
    department: '',
    status: 1,
    isAdmin: 0,
    isWeeklyAdmin: 0,
    isMonthlyAdmin: 0,
    weeklyPermission: 0,
    monthlyPermission: 0,
    auditPermission: 0
  })

  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 加载部门列表
const loadDeptList = async () => {
  try {
    const response = await deptApi.getTree()
    if (response.data) {
      deptOptions.value = response.data
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
    // 暂时提供默认部门选项，避免页面报错
    deptOptions.value = [
      { id: 1, name: '总公司', children: [] }
    ]
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadUserList()
  loadDeptList()
})
</script>

<style scoped>
.user-management {
  padding: 0;
}
</style>