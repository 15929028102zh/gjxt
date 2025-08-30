import request from '@/utils/request'

// 周检记录管理API
export const inspectionApi = {
  // 分页查询周检记录列表
  getPage(params) {
    return request({
      url: '/inspection-record/page',
      method: 'get',
      params
    })
  },
  
  // 根据ID查询周检记录详情
  getById(id) {
    return request({
      url: `/inspection-record/${id}`,
      method: 'get'
    })
  },
  
  // 新增周检记录
  create(data) {
    return request({
      url: '/inspection-record/submit',
      method: 'post',
      data
    })
  },
  
  // 更新周检记录
  update(data) {
    return request({
      url: '/inspection-record',
      method: 'put',
      data
    })
  },
  
  // 删除周检记录
  delete(id) {
    return request({
      url: `/inspection-record/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除周检记录
  batchDelete(ids) {
    return request({
      url: '/inspection-record/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 确认整改
  confirmRectification(id, data) {
    return request({
      url: `/inspection-record/${id}/rectify`,
      method: 'put',
      data
    })
  },
  
  // 确认复查
  confirmReview(id, data) {
    return request({
      url: `/inspection-record/${id}/review`,
      method: 'put',
      data
    })
  },
  
  // 更新记录状态
  updateStatus(id, status) {
    return request({
      url: `/inspection-record/${id}/status`,
      method: 'put',
      data: { status }
    })
  },
  
  // 导出周检记录
  exportRecords(params) {
    return request({
      url: '/inspection-record/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

// 图片上传API
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