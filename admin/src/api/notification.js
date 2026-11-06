import request from '@/utils/request'

/**
 * 通知相关API
 */

// 获取当前用户通知列表（从token解析用户ID）
export const getUserNotifications = () => {
  return request({
    url: `/notifications/user/notifications`,
    method: 'get'
  })
}

// 标记通知为已读
export const markNotificationAsRead = (notificationId) => {
  return request({
    url: `/notifications/read/${notificationId}`,
    method: 'put'
  })
}

// 删除通知
export const deleteNotification = (notificationId) => {
  return request({
    url: `/notifications/${notificationId}`,
    method: 'delete'
  })
}

// 获取未读通知数量
export const getUnreadNotificationCount = () => {
  return request({
    url: `/notifications/unread-count`,
    method: 'get'
  })
}

// 批量标记通知为已读
export const markAllNotificationsAsRead = () => {
  return request({
    url: `/notifications/read-all`,
    method: 'put'
  })
}

// 清空用户所有通知
export const clearAllNotifications = () => {
  return request({
    url: `/notifications/clear`,
    method: 'delete'
  })
}

// 获取当前用户的月检任务通知列表
export const getTaskNotifications = () => {
  return request({
    url: '/notifications/tasks',
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