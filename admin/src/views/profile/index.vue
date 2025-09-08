<template>
  <div class="profile-container">
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">个人中心</h1>
      </div>
      
      <!-- 用户信息卡片 -->
      <div class="profile-content">
        <el-row :gutter="20">
          <!-- 左侧用户信息展示 -->
          <el-col :span="8">
            <el-card class="user-info-card">
              <div class="user-avatar-section">
                <div class="avatar-container">
                  <el-avatar 
                    :size="120" 
                    :src="convertToHttpUrl(userInfo.imageUrl) || '/default-avatar.svg'"
                    class="user-avatar"
                  />
                  <el-button 
                    type="primary" 
                    size="small" 
                    class="change-avatar-btn"
                    @click="showAvatarDialog = true"
                  >
                    更换头像
                  </el-button>
                </div>
                <div class="user-basic-info">
                  <h3 class="user-name">{{ userInfo.realName || '未设置' }}</h3>
                  <p class="user-username">@{{ userInfo.username }}</p>
                  <p class="user-phone">{{ userInfo.phone || '未设置手机号' }}</p>
                  <p class="user-email">{{ userInfo.email || '未设置邮箱' }}</p>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <!-- 右侧编辑表单 -->
          <el-col :span="16">
            <el-card class="edit-form-card">
              <template #header>
                <div class="card-header">
                  <span>编辑个人信息</span>
                </div>
              </template>
              
              <el-form
                ref="formRef"
                :model="editForm"
                :rules="formRules"
                label-width="100px"
                class="profile-form"
              >
                <el-form-item label="真实姓名" prop="realName">
                  <el-input 
                    v-model="editForm.realName" 
                    placeholder="请输入真实姓名"
                    maxlength="20"
                    show-word-limit
                  />
                </el-form-item>
                
                <el-form-item label="手机号码" prop="phone">
                  <el-input 
                    v-model="editForm.phone" 
                    placeholder="请输入手机号码"
                    maxlength="11"
                  />
                </el-form-item>
                
                <el-form-item label="邮箱地址" prop="email">
                  <el-input 
                    v-model="editForm.email" 
                    placeholder="请输入邮箱地址"
                    maxlength="50"
                  />
                </el-form-item>
                
                <el-form-item label="用户名">
                  <el-input 
                    v-model="editForm.username" 
                    disabled
                    placeholder="用户名不可修改"
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button 
                    type="primary" 
                    @click="saveProfile"
                    :loading="saveLoading"
                  >
                    保存修改
                  </el-button>
                  <el-button @click="resetForm">
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
    
    <!-- 更换头像对话框 -->
    <el-dialog
      v-model="showAvatarDialog"
      title="更换头像"
      width="500px"
      @close="resetAvatarDialog"
    >
      <div class="avatar-upload-section">
        <div class="current-avatar">
          <h4>当前头像</h4>
          <el-avatar 
            :size="80" 
            :src="userInfo.imageUrl || '/default-avatar.svg'"
          />
        </div>
        
        <div class="upload-area">
          <h4>选择新头像</h4>
          <el-upload
            ref="uploadRef"
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :on-error="handleAvatarError"
            :before-upload="beforeAvatarUpload"
            accept="image/*"
          >
            <div class="upload-placeholder">
              <el-icon class="upload-icon"><Plus /></el-icon>
              <div class="upload-text">点击上传头像</div>
              <div class="upload-tip">支持 JPG、PNG 格式，文件大小不超过 2MB</div>
            </div>
          </el-upload>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAvatarDialog = false">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { userApi } from '@/api/system'
import { getToken } from '@/utils/token'

// 响应式数据
const formRef = ref()
const uploadRef = ref()
const saveLoading = ref(false)
const showAvatarDialog = ref(false)

// 用户信息
const userInfo = ref({
  id: '',
  username: '',
  realName: '',
  phone: '',
  email: '',
  imageUrl: ''
})

// 编辑表单
const editForm = reactive({
  realName: '',
  phone: '',
  email: '',
  username: ''
})

// 表单验证规则
const formRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '真实姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 上传配置
const uploadUrl = import.meta.env.VITE_API_BASE_URL + '/file/upload'
const uploadHeaders = {
  'Authorization': 'Bearer ' + getToken()
}

// 加载用户信息
const loadUserInfo = async () => {
  try {
    // 先从本地存储获取用户信息
    const localUserInfo = localStorage.getItem('userInfo')
    if (localUserInfo) {
      const parsedUserInfo = JSON.parse(localUserInfo)
      userInfo.value = { ...parsedUserInfo }
      
      // 填充编辑表单
      editForm.realName = parsedUserInfo.realName || ''
      editForm.phone = parsedUserInfo.phone || ''
      editForm.email = parsedUserInfo.email || ''
      editForm.username = parsedUserInfo.username || ''
    }
    
    // 再通过API获取最新信息
    const response = await userApi.getUserInfo(userInfo.value.username)
    if (response.code === 200) {
      userInfo.value = { ...response.data }
      
      // 更新编辑表单
      editForm.realName = response.data.realName || ''
      editForm.phone = response.data.phone || ''
      editForm.email = response.data.email || ''
      editForm.username = response.data.username || ''
      
      // 更新本地存储
      localStorage.setItem('userInfo', JSON.stringify(response.data))
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
    ElMessage.error('加载用户信息失败')
  }
}

// 保存用户信息
const saveProfile = async () => {
  try {
    // 表单验证
    await formRef.value.validate()
    
    saveLoading.value = true
    
    // 调用更新API
    const response = await userApi.update({
      id: userInfo.value.id,
      realName: editForm.realName,
      phone: editForm.phone,
      email: editForm.email
    })
    
    if (response.code === 200) {
      // 更新本地用户信息
      userInfo.value.realName = editForm.realName
      userInfo.value.phone = editForm.phone
      userInfo.value.email = editForm.email
      
      // 更新本地存储
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
      
      ElMessage.success('保存成功')
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存用户信息失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saveLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  editForm.realName = userInfo.value.realName || ''
  editForm.phone = userInfo.value.phone || ''
  editForm.email = userInfo.value.email || ''
  editForm.username = userInfo.value.username || ''
  
  formRef.value?.clearValidate()
}

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 头像上传成功
const handleAvatarSuccess = async (response) => {
  try {
    if (response.code === 200) {
      const imageUrl = response.data.url || response.data
      
      // 调用更新头像API
      const updateResponse = await userApi.updateAvatar(userInfo.value.id, imageUrl)
      
      if (updateResponse.code === 200) {
        // 更新本地用户信息
        userInfo.value.imageUrl = imageUrl
        
        // 更新本地存储
        localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
        
        ElMessage.success('头像更新成功')
        showAvatarDialog.value = false
      } else {
        ElMessage.error(updateResponse.message || '头像更新失败')
      }
    } else {
      ElMessage.error(response.message || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败')
  }
}

// 头像上传失败
const handleAvatarError = (error) => {
  console.error('头像上传失败:', error)
  ElMessage.error('头像上传失败')
}

// 重置头像对话框
const resetAvatarDialog = () => {
  uploadRef.value?.clearFiles()
}

// 将相对路径转换为HTTP访问路径
const convertToHttpUrl = (relativePath) => {
  if (!relativePath || relativePath.trim() === '') {
    return ''
  }
  
  // 如果已经是完整的HTTP/HTTPS URL，直接返回
  if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
    return relativePath
  }
  
  // 直接拼接完整的HTTP URL
  // 后端返回相对路径，如: /files/images/20250831/xxx.jpg
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://192.168.244.128:8080'
  const fullUrl = `${baseUrl}/api${relativePath}`
  return fullUrl
}

// 组件挂载时加载数据
onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 0;
}

.page-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.profile-content {
  margin-top: 20px;
}

.user-info-card {
  height: 100%;
}

.user-avatar-section {
  text-align: center;
  padding: 20px 0;
}

.avatar-container {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.user-avatar {
  border: 4px solid #f0f0f0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.change-avatar-btn {
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 12px;
}

.user-basic-info {
  text-align: center;
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.user-username {
  font-size: 14px;
  color: #909399;
  margin: 0 0 8px 0;
}

.user-phone,
.user-email {
  font-size: 14px;
  color: #606266;
  margin: 0 0 8px 0;
}

.edit-form-card {
  height: 100%;
}

.card-header {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.profile-form {
  padding: 20px 0;
}

.avatar-upload-section {
  display: flex;
  justify-content: space-around;
  align-items: flex-start;
  padding: 20px 0;
}

.current-avatar,
.upload-area {
  text-align: center;
  flex: 1;
}

.current-avatar h4,
.upload-area h4 {
  margin: 0 0 15px 0;
  font-size: 14px;
  color: #606266;
}

.avatar-uploader {
  display: inline-block;
}

.upload-placeholder {
  width: 120px;
  height: 120px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-placeholder:hover {
  border-color: #409eff;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}

.dialog-footer {
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-content .el-col {
    margin-bottom: 20px;
  }
  
  .avatar-upload-section {
    flex-direction: column;
    gap: 20px;
  }
}
</style>