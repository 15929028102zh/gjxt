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

  // 删除月检记录（录入页：公示期前直接删除，与提交页的 delete 不是同一条路径）
  entryDelete(id) {
    return request({
      url: `/monthly-inspection-record/${id}/entry-delete`,
      method: 'delete'
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
  },

  // 回退记录（发送审核通知）
  rejectRecord(id) {
    return request({
      url: `/monthly-inspection-record/${id}/reject`,
      method: 'post'
    })
  },

  // 撤回记录（公示期前直接回到待提交，无需审核）
  withdrawRecord(id) {
    return request({
      url: `/monthly-inspection-record/${id}/withdraw`,
      method: 'post'
    })
  },

  // 查询待审核记录
  getAuditRecords(params) {
    return request({
      url: '/monthly-inspection-record/audit-list',
      method: 'get',
      params
    })
  },

  // 审核确认
  confirmAudit(id, data) {
    return request({
      url: `/monthly-inspection-record/${id}/audit`,
      method: 'put',
      data
    })
  },
  
  // 拒绝审核（将状态从待审核(3)改回已提交(1)）
  rejectAudit(id) {
    return request({
      url: `/monthly-inspection-record/${id}/reject-audit`,
      method: 'put'
    })
  },

  // 提交编辑审核（公示期内编辑/删除前）
  submitEditAudit(id, data) {
    return request({
      url: `/monthly-inspection-record/${id}/submit-edit-audit`,
      method: 'post',
      data
    })
  },

  // 审核通过编辑申请
  confirmEditAudit(id, data) {
    return request({
      url: `/monthly-inspection-record/${id}/confirm-edit-audit`,
      method: 'put',
      data
    })
  },

  // 审核拒绝编辑申请
  rejectEditAudit(id, data) {
    return request({
      url: `/monthly-inspection-record/${id}/reject-edit-audit`,
      method: 'put',
      data
    })
  }
}

// 月检记录 API 别名（兼容历史命名）
export const monthlyInspectionRecordApi = monthlyRecordApi

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
  },
  getByTaskNo(taskNo) {
    return request({
      url: `/monthly-inspection-task/task-no/${taskNo}`,
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

// 获取全部用户选项API (检查人、责任人下拉,不分页)
export const getUserOptions = () => {
  return request({
    url: '/user/list',
    method: 'get'
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

// 获取专业列表API - 从岗检标准接口获取
export const getProfessionList = (params) => {
  return request({
    url: '/gjbz-data/options/types',
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

// ========== 移动端兼容的级联接口 ==========
// 以下级联接口均支持可选 departmentId：后端据受检单位自动按标准组过滤
// 获取专业类别选项（移动端兼容）
export const getTypeOptions = (departmentId) => {
  return request({
    url: '/gjbz-data/options/types',
    method: 'get',
    params: departmentId ? { departmentId } : {}
  })
}

// 获取模块选项（移动端兼容）
export const getModuleOptions = () => {
  return request({
    url: '/gjbz-data/options/modules',
    method: 'get'
  })
}

// 获取责任选项（移动端兼容）
export const getResponsibilityOptions = () => {
  return request({
    url: '/gjbz-data/options/responsibilities',
    method: 'get'
  })
}

// 获取问题选项（移动端兼容）
export const getIssueOptions = () => {
  return request({
    url: '/gjbz-data/options/issues',
    method: 'get'
  })
}

// 获取问题选项（包含质量等级）
export const getIssueOptionsWithQuality = (departmentId) => {
  return request({
    url: '/gjbz-data/options/issues-with-quality',
    method: 'get',
    params: departmentId ? { departmentId } : {}
  })
}

export const getResponsibilitiesByModuleName = (moduleName, departmentId) => {
  return request({
    url: `/gjbz-data/options/responsibilities/by-module/${encodeURIComponent(moduleName)}`,
    method: 'get',
    params: departmentId ? { departmentId } : {}
  })
}

export const getIssuesByResponsibilityName = (responsibilityName, departmentId) => {
  return request({
    url: `/gjbz-data/options/issues/by-responsibility/${encodeURIComponent(responsibilityName)}`,
    method: 'get',
    params: departmentId ? { departmentId } : {}
  })
}

export const getModulesByTypeName = (typeName, departmentId) => {
  return request({
    url: `/gjbz-data/modules/by-type/${encodeURIComponent(typeName)}`,
    method: 'get',
    params: departmentId ? { departmentId } : {}
  })
}
