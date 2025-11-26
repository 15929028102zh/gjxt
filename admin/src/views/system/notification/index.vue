<template>
  <div class="notification-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>通知管理</span>
          <el-button type="primary" @click="showPushDialog">
            <el-icon><Bell /></el-icon>
            推送通知
          </el-button>
        </div>
      </template>

      <!-- 通知列表 -->
      <el-table :data="notifications" v-loading="loading" style="width: 100%">
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityTagType(row.priority)">
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetType" label="推送对象" width="120">
          <template #default="{ row }">
            <el-tag :type="row.targetType === 'department' ? 'warning' : 'success'">
              {{ row.targetType === 'department' ? '部门' : '用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="deleteNotification(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; text-align: right"
      />
    </el-card>

    <!-- 推送通知对话框 -->
    <el-dialog
      v-model="pushDialogVisible"
      title="推送通知"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="pushFormRef"
        :model="pushForm"
        :rules="pushFormRules"
        label-width="100px"
      >
        <el-form-item label="推送类型" prop="pushType">
          <el-radio-group v-model="pushForm.pushType" @change="handlePushTypeChange">
            <el-radio value="department">推送到部门</el-radio>
            <el-radio value="user">推送到用户</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item 
          v-if="pushForm.pushType === 'department'" 
          label="选择部门" 
          prop="departmentId"
        >
          <el-select
            v-model="pushForm.departmentId"
            placeholder="请选择部门"
            style="width: 100%"
            filterable
            @change="handleDepartmentChange"
          >
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.deptName || dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item 
          v-if="pushForm.pushType === 'user'" 
          label="选择用户" 
          prop="userId"
        >
          <el-select
            v-model="pushForm.userId"
            placeholder="请选择用户"
            style="width: 100%"
            filterable
            @change="handleUserChange"
          >
            <el-option
              v-for="user in users"
              :key="user.id"
              :label="`${user.realName} (${user.username})`"
              :value="user.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="通知标题" prop="title">
          <el-input
            v-model="pushForm.title"
            placeholder="请输入通知标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="通知内容" prop="content">
          <el-input
            v-model="pushForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入通知内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="优先级" prop="priority">
          <el-select v-model="pushForm.priority" placeholder="请选择优先级">
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="操作链接" prop="actionUrl">
          <el-input
            v-model="pushForm.actionUrl"
            placeholder="请输入操作链接（可选）"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handlePushNotification" :loading="pushing">
            推送
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell } from '@element-plus/icons-vue'
import { getNotificationsPage, deleteNotificationById, pushNotificationToDepartment, pushNotificationToUser } from '@/api/notification'
import { deptApi } from '@/api/system'
import { userApi } from '@/api/system'

// 响应式数据
const loading = ref(false)
const notifications = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 推送对话框
const pushDialogVisible = ref(false)
const pushing = ref(false)
const pushFormRef = ref()
const departments = ref([])
const users = ref([])

// 推送表单
const pushForm = reactive({
  pushType: 'department',
  departmentId: '',
  departmentName: '',
  userId: '',
  userName: '',
  title: '',
  content: '',
  priority: 2,
  actionUrl: ''
})

// 表单验证规则
const pushFormRules = {
  pushType: [
    { required: true, message: '请选择推送类型', trigger: 'change' }
  ],
  departmentId: [
    { 
      required: true, 
      message: '请选择部门', 
      trigger: 'change',
      validator: (rule, value, callback) => {
        if (pushForm.pushType === 'department' && !value) {
          callback(new Error('请选择部门'))
        } else {
          callback()
        }
      }
    }
  ],
  userId: [
    { 
      required: true, 
      message: '请选择用户', 
      trigger: 'change',
      validator: (rule, value, callback) => {
        if (pushForm.pushType === 'user' && !value) {
          callback(new Error('请选择用户'))
        } else {
          callback()
        }
      }
    }
  ],
  title: [
    { required: true, message: '请输入通知标题', trigger: 'blur' },
    { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入通知内容', trigger: 'blur' },
    { min: 1, max: 500, message: '内容长度在 1 到 500 个字符', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ]
}

// 生命周期
onMounted(() => {
  loadNotifications()
  loadDepartments()
  loadUsers()
})

// 加载通知列表
const loadNotifications = async () => {
  loading.value = true
  try {
    const response = await getNotificationsPage({ current: currentPage.value, size: pageSize.value })
    const data = response?.data
    notifications.value = Array.isArray(data?.records) ? data.records : (Array.isArray(data) ? data : [])
    total.value = data?.total || notifications.value.length || 0
  } catch (error) {
    console.error('加载通知列表失败:', error)
    ElMessage.error('加载通知列表失败')
  } finally {
    loading.value = false
  }
}

// 加载部门列表
const loadDepartments = async () => {
  try {
    // 统一使用分页接口，确保兼容后端参数（current/size）
    const response = await deptApi.getPage({ current: 1, size: 1000 })
    const data = response?.data
    const list = Array.isArray(data?.records) ? data.records : (Array.isArray(data) ? data : [])
    departments.value = list
  } catch (error) {
    console.error('加载部门列表失败:', error)
    departments.value = []
  }
}

// 加载用户列表
const loadUsers = async () => {
  try {
    const response = await userApi.getPage({ current: 1, size: 1000 })
    const data = response?.data
    const list = Array.isArray(data?.records) ? data.records : (Array.isArray(data) ? data : [])
    users.value = list
  } catch (error) {
    console.error('加载用户列表失败:', error)
    users.value = []
  }
}

// 显示推送对话框
const showPushDialog = () => {
  pushDialogVisible.value = true
  resetPushForm()
}

// 重置推送表单
const resetPushForm = () => {
  Object.assign(pushForm, {
    pushType: 'department',
    departmentId: '',
    departmentName: '',
    userId: '',
    userName: '',
    title: '',
    content: '',
    priority: 2,
    actionUrl: ''
  })
  pushFormRef.value?.clearValidate()
}

// 处理推送类型变化
const handlePushTypeChange = () => {
  pushForm.departmentId = ''
  pushForm.departmentName = ''
  pushForm.userId = ''
  pushForm.userName = ''
  pushFormRef.value?.clearValidate(['departmentId', 'userId'])
}

// 处理部门选择
const handleDepartmentChange = (deptId) => {
  const dept = departments.value.find(d => d.id === deptId)
  pushForm.departmentName = (dept?.deptName || dept?.name || '')
}

// 处理用户选择
const handleUserChange = (userId) => {
  const user = users.value.find(u => u.id === userId)
  pushForm.userName = user?.realName || user?.username || ''
}

// 推送通知
const handlePushNotification = async () => {
  try {
    await pushFormRef.value?.validate()
    
    pushing.value = true
    
    const pushData = {
      title: pushForm.title,
      content: pushForm.content,
      priority: pushForm.priority,
      actionUrl: pushForm.actionUrl || null
    }

    let response
    if (pushForm.pushType === 'department') {
      // 推送到部门
      response = await pushNotificationToDepartment({
        ...pushData,
        departmentId: pushForm.departmentId,
        departmentName: pushForm.departmentName
      })
    } else {
      // 推送到用户
      response = await pushNotificationToUser({
        ...pushData,
        userId: pushForm.userId,
        userName: pushForm.userName
      })
    }

    ElMessage.success('通知推送成功')
    pushDialogVisible.value = false
    loadNotifications()
  } catch (error) {
    console.error('推送通知失败:', error)
    ElMessage.error('推送通知失败')
  } finally {
    pushing.value = false
  }
}

// 删除通知
const deleteNotification = async (notificationId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条通知吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteNotificationById(notificationId)
    ElMessage.success('删除成功')
    loadNotifications()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除通知失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 关闭对话框
const handleDialogClose = () => {
  pushDialogVisible.value = false
  resetPushForm()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  loadNotifications()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadNotifications()
}

// 获取类型标签类型
const getTypeTagType = (type) => {
  const typeMap = {
    'system': 'info',
    'task': 'warning',
    'alert': 'danger'
  }
  return typeMap[type] || 'info'
}

// 获取类型文本
const getTypeText = (type) => {
  const typeMap = {
    'system': '系统',
    'task': '任务',
    'alert': '警告'
  }
  return typeMap[type] || '未知'
}

// 获取优先级标签类型
const getPriorityTagType = (priority) => {
  const priorityMap = {
    1: 'info',
    2: 'warning', 
    3: 'danger'
  }
  return priorityMap[priority] || 'info'
}

// 获取优先级文本
const getPriorityText = (priority) => {
  const priorityMap = {
    1: '低',
    2: '中',
    3: '高'
  }
  return priorityMap[priority] || '未知'
}
</script>

<style scoped>
.notification-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>