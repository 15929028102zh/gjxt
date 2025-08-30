import request from '@/utils/request'

// 登录API
export const login = (data) => {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 用户管理API
export const userApi = {
  // 分页查询用户列表
  getPage(params) {
    return request({
      url: '/user/page',
      method: 'get',
      params
    })
  },
  
  // 根据ID查询用户详情
  getById(id) {
    return request({
      url: `/user/${id}`,
      method: 'get'
    })
  },
  
  // 获取当前用户信息
  getUserInfo(username) {
    return request({
      url: '/user/info',
      method: 'get',
      params: { username }
    })
  },
  
  // 新增用户
  create(data) {
    return request({
      url: '/user',
      method: 'post',
      data
    })
  },
  
  // 更新用户
  update(data) {
    return request({
      url: '/user',
      method: 'put',
      data
    })
  },
  
  // 删除用户
  delete(id) {
    return request({
      url: `/user/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除用户
  batchDelete(ids) {
    return request({
      url: '/user/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 重置用户密码
  resetPassword(id, newPassword) {
    return request({
      url: `/user/${id}/reset-password`,
      method: 'put',
      data: newPassword ? { newPassword } : {}
    })
  },
  
  // 修改用户密码
  changePassword(id, oldPassword, newPassword) {
    return request({
      url: `/user/${id}/change-password`,
      method: 'put',
      data: { oldPassword, newPassword }
    })
  },
  
  // 启用/禁用用户
  updateStatus(id, status) {
    return request({
      url: `/user/${id}/status`,
      method: 'put',
      data: { status }
    })
  },
  
  // 检查用户名是否存在
  checkUsername(username, excludeId) {
    return request({
      url: '/user/check-username',
      method: 'get',
      params: { username, excludeId }
    })
  }
}

// 部门管理API
export const deptApi = {
  // 获取部门树
  getTree(params) {
    return request({
      url: '/dept/tree',
      method: 'get',
      params
    })
  },
  
  // 分页查询部门列表
  getPage(params) {
    return request({
      url: '/dept/page',
      method: 'get',
      params
    })
  },
  
  // 根据ID查询部门详情
  getById(id) {
    return request({
      url: `/dept/${id}`,
      method: 'get'
    })
  },
  
  // 新增部门
  create(data) {
    return request({
      url: '/dept',
      method: 'post',
      data
    })
  },
  
  // 更新部门
  update(data) {
    return request({
      url: '/dept',
      method: 'put',
      data
    })
  },
  
  // 删除部门
  delete(id) {
    return request({
      url: `/dept/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除部门
  batchDelete(ids) {
    return request({
      url: '/dept/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 更新部门状态
  updateStatus(id, status) {
    return request({
      url: `/dept/status/${id}/${status}`,
      method: 'put'
    })
  },
  
  // 检查部门名称是否存在
  checkDeptName(deptName, excludeId) {
    return request({
      url: '/dept/check-name',
      method: 'get',
      params: { deptName, excludeId }
    })
  },
  
  // 检查部门是否有子部门
  hasChild(deptId) {
    return request({
      url: `/dept/has-child/${deptId}`,
      method: 'get'
    })
  }
}

// 字典管理API
export const dictApi = {
  // 分页查询字典列表
  getPage(params) {
    return request({
      url: '/dictionary/page',
      method: 'get',
      params
    })
  },
  
  // 根据类型查询字典
  getByType(dictType) {
    return request({
      url: `/dictionary/type/${dictType}`,
      method: 'get'
    })
  },
  
  // 获取所有字典数据（按类型分组）
  getAllData() {
    return request({
      url: '/dictionary/all-data',
      method: 'get'
    })
  },
  
  // 获取所有字典类型
  getAllTypes() {
    return request({
      url: '/dictionary/types',
      method: 'get'
    })
  },
  
  // 根据ID查询字典详情
  getById(id) {
    return request({
      url: `/dictionary/${id}`,
      method: 'get'
    })
  },
  
  // 新增字典
  create(data) {
    return request({
      url: '/dictionary',
      method: 'post',
      data
    })
  },
  
  // 更新字典
  update(data) {
    return request({
      url: '/dictionary',
      method: 'put',
      data
    })
  },
  
  // 删除字典
  delete(id) {
    console.log('dictApi.delete被调用，ID:', id)
    return request({
      url: `/dictionary/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除字典
  batchDelete(ids) {
    return request({
      url: '/dictionary/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 检查字典标签是否存在
  checkLabel(dictType, dictLabel, excludeId) {
    return request({
      url: '/dictionary/check-label',
      method: 'get',
      params: { dictType, dictLabel, excludeId }
    })
  },
  
  // 检查字典键值是否存在
  checkValue(dictType, dictValue, excludeId) {
    return request({
      url: '/dictionary/check-value',
      method: 'get',
      params: { dictType, dictValue, excludeId }
    })
  }
}