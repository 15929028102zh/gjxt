import request from '@/utils/request'

// 月检记录管理API
export const monthlyRecordApi = {
  // 分页查询月检记录列表
  getPage(params) {
    return request({
      url: '/monthly-inspection-record/page',
      method: 'get',
      params
    })
  },
  
  // 根据ID查询月检记录详情
  getById(id) {
    return request({
      url: `/monthly-inspection-record/${id}`,
      method: 'get'
    })
  },
  
  // 新增月检记录
  create(data) {
    return request({
      url: '/monthly-inspection-record',
      method: 'post',
      data
    })
  },
  
  // 更新月检记录
  update(data) {
    return request({
      url: '/monthly-inspection-record',
      method: 'put',
      data
    })
  },
  
  // 删除月检记录
  delete(id) {
    return request({
      url: `/monthly-inspection-record/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除月检记录
  batchDelete(ids) {
    return request({
      url: '/monthly-inspection-record/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 确认整改
  confirmRectification(id, data) {
    return request({
      url: `/monthly-inspection-record/${id}/rectify`,
      method: 'put',
      data
    })
  },
  
  // 确认复查
  confirmReview(id, data) {
    return request({
      url: `/monthly-inspection-record/${id}/review`,
      method: 'put',
      data
    })
  },
  
  // 更新记录状态
  updateStatus(data) {
    return request({
      url: `/monthly-inspection-record/status`,
      method: 'put',
      data
    })
  },

  // 批量更新记录状态
  batchUpdateStatus(data) {
    return request({
      url: `/monthly-inspection-record/batch-status`,
      method: 'put',
      data
    })
  },
  
  // 导出月检记录
  exportRecords(params) {
    return request({
      url: '/monthly-inspection-record/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  },

  // 获取延迟整改记录列表
  getDelayedRecords(params) {
    return request({
      url: '/monthly-inspection-record/delayed',
      method: 'get',
      params
    })
  }
}

// 月检任务管理API
export const monthlyTaskApi = {
  // 分页查询月检任务列表
  getPage(params) {
    return request({
      url: '/monthly-inspection-task/page',
      method: 'get',
      params
    })
  },
  
  // 根据ID查询月检任务详情
  getById(id) {
    return request({
      url: `/monthly-inspection-task/${id}`,
      method: 'get'
    })
  },
  
  // 新增月检任务
  create(data) {
    return request({
      url: '/monthly-inspection-task',
      method: 'post',
      data
    })
  },
  
  // 更新月检任务
  update(data) {
    return request({
      url: '/monthly-inspection-task',
      method: 'put',
      data
    })
  },
  
  // 删除月检任务
  delete(id) {
    return request({
      url: `/monthly-inspection-task/${id}`,
      method: 'delete'
    })
  },
  
  // 获取用户的月检任务通知
  getUserTasks(userId) {
    return request({
      url: `/monthly-inspection-task/user/${userId}`,
      method: 'get'
    })
  }
}

// 图片上传API (复用)
export const uploadApi = {
  // 上传图片
  uploadImage(formData) {
    return request({
      url: '/upload/image',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

// 获取字典数据的API (复用系统API中的字典接口)
export const getDictByType = (dictType) => {
  return request({
    url: `/dictionary/type/${dictType}`,
    method: 'get'
  })
}

// 获取用户列表API (检查人、复查人等)
export const getUserList = (params) => {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

// 获取部门列表API
export const getDeptList = (params) => {
  return request({
    url: '/dept/list',
    method: 'get',
    params
  })
}

// 获取专业列表API
export const getProfessionList = (params) => {
  return request({
    url: '/dictionary/type/专业',
    method: 'get',
    params
  })
}

// 获取性质界定列表API
export const getNatureDefinitionList = (params) => {
  return request({
    url: '/dictionary/type/性质界定',
    method: 'get',
    params
  })
}

// 获取模块列表API
export const getModuleList = (params) => {
  return request({
    url: '/module/list',
    method: 'get',
    params
  })
}

// 根据专业ID获取模块列表API
export const getModuleByProfessionId = (professionId) => {
  return request({
    url: `/module/profession/${professionId}`,
    method: 'get'
  })
}

// 获取责任列表API
export const getResponsibilityList = (params) => {
  return request({
    url: '/responsibility/list',
    method: 'get',
    params
  })
}

// 根据模块ID获取责任列表API
export const getResponsibilityByModuleId = (moduleId) => {
  return request({
    url: `/responsibility/module/${moduleId}`,
    method: 'get'
  })
}

// 获取问题列表API
export const getProblemList = (params) => {
  return request({
    url: '/problem/list',
    method: 'get',
    params
  })
}

// 根据责任ID获取问题列表API
export const getProblemByResponsibilityId = (responsibilityId) => {
  return request({
    url: `/problem/responsibility/${responsibilityId}`,
    method: 'get'
  })
}