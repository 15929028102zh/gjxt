import request from '@/utils/request'

/**
 * 通知相关API
 */

// 获取用户通知列表
export const getUserNotifications = (userId) => {
  return request({
    url: `/notifications/user/${userId}`,
    method: 'get'
  })
}

// 标记通知为已读
export const markNotificationAsRead = (userId, notificationId) => {
  return request({
    url: `/notifications/user/${userId}/read/${notificationId}`,
    method: 'put'
  })
}

// 删除通知
export const deleteNotification = (userId, notificationId) => {
  return request({
    url: `/notifications/user/${userId}/${notificationId}`,
    method: 'delete'
  })
}

// 获取未读通知数量
export const getUnreadNotificationCount = (userId) => {
  return request({
    url: `/notifications/user/${userId}/unread-count`,
    method: 'get'
  })
}

// 批量标记通知为已读
export const markAllNotificationsAsRead = (userId) => {
  return request({
    url: `/notifications/user/${userId}/read-all`,
    method: 'put'
  })
}

// 清空用户所有通知
export const clearAllNotifications = (userId) => {
  return request({
    url: `/notifications/user/${userId}/clear`,
    method: 'delete'
  })
}

// 获取用户的月检任务通知列表（已通知状态的月检任务）
export const getTaskNotifications = (userId) => {
  return request({
    url: `/notifications/user/${userId}/tasks`,
    method: 'get'
  })
}

// 管理端：分页获取所有通知
export const getNotificationsPage = (params) => {
  return request({
    url: '/notifications/page',
    method: 'get',
    params
  })
}

// 管理端：按ID删除通知
export const deleteNotificationById = (id) => {
  return request({
    url: `/notifications/${id}`,
    method: 'delete'
  })
}

// 手动推送通知到部门
export const pushNotificationToDepartment = (data) => {
  return request({
    url: '/notifications/push-to-department',
    method: 'post',
    data
  })
}

// 手动推送通知到用户
export const pushNotificationToUser = (data) => {
  return request({
    url: '/notifications/push-to-user',
    method: 'post',
    data
  })
}

export default {
  getUserNotifications,
  markNotificationAsRead,
  deleteNotification,
  getUnreadNotificationCount,
  markAllNotificationsAsRead,
  clearAllNotifications,
  getTaskNotifications,
  getNotificationsPage,
  deleteNotificationById,
  pushNotificationToDepartment,
  pushNotificationToUser
}