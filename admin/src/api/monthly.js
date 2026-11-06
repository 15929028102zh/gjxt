import request from '@/utils/request'

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
  
  // 批量删除月检任务
  batchDelete(ids) {
    return request({
      url: '/monthly-inspection-task/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 根据用户ID获取任务
  getByUserId(userId, params) {
    return request({
      url: `/monthly-inspection-task/user/${userId}`,
      method: 'get',
      params
    })
  },
  
  // 根据任务编号获取任务
  getByTaskNo(taskNo) {
    return request({
      url: `/monthly-inspection-task/task-no/${taskNo}`,
      method: 'get'
    })
  },
  
  // 更新任务状态
  updateStatus(id, status) {
    return request({
      url: `/monthly-inspection-task/${id}/status`,
      method: 'put',
      data: { status }
    })
  },
  
  // 批量更新任务状态
  batchUpdateStatus(ids, status) {
    return request({
      url: '/monthly-inspection-task/batch/status',
      method: 'put',
      data: { ids, status }
    })
  },
  
  // 发送任务通知
  sendNotification(id) {
    return request({
      url: `/monthly-inspection-task/${id}/notify`,
      method: 'post'
    })
  },
  
  // 批量发送任务通知
  batchSendNotification(ids) {
    return request({
      url: '/monthly-inspection-task/batch/notify',
      method: 'post',
      data: ids
    })
  },
  
  // 获取用户任务统计
  getUserStatistics(userId) {
    return request({
      url: `/monthly-inspection-task/user/${userId}/statistics`,
      method: 'get'
    })
  },
  
  // 获取状态统计
  getStatusStatistics(params) {
    return request({
      url: '/monthly-inspection-task/statistics/status',
      method: 'get',
      params
    })
  },
  
  // 获取部门统计
  getDepartmentStatistics(params) {
    return request({
      url: '/monthly-inspection-task/statistics/department',
      method: 'get',
      params
    })
  },
  
  // 获取专业统计
  getProfessionStatistics(params) {
    return request({
      url: '/monthly-inspection-task/statistics/profession',
      method: 'get',
      params
    })
  }
}

// 专业管理API
export const professionApi = {
  // 获取专业列表
  getList(params) {
    return request({
      url: '/dictionary/type/专业',
      method: 'get',
      params
    })
  }
}