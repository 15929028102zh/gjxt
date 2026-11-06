/**
 * 权限工具函数
 * V3 拆分:is_admin 已废弃,改用 isWeeklyAdmin/isMonthlyAdmin
 */

/**
 * 检查用户是否为周检管理员(isWeeklyAdmin=1, 兼容老 isAdmin)
 */
export function isWeeklyAdmin() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1') return true
    return userInfo.isWeeklyAdmin === 1 || userInfo.isWeeklyAdmin === true || userInfo.isWeeklyAdmin === '1'
  } catch (error) {
    return false
  }
}

/**
 * 检查用户是否为月检管理员
 * V3 严格判断:只看 isMonthlyAdmin=1
 * 周检管理员/老 isAdmin=1 不再视为月检管理员(V3 已拆分周/月管理员)
 * 与 Pinia store (user.js) / 路由守卫保持一致
 */
export function isMonthlyAdmin() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    return userInfo.isMonthlyAdmin === 1 || userInfo.isMonthlyAdmin === true || userInfo.isMonthlyAdmin === '1'
  } catch (error) {
    return false
  }
}

/**
 * 检查用户是否有指定权限
 * @param {string} permission 权限标识
 * @returns {boolean} 是否有权限
 */
export function hasPermission(permission) {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

    // 任一管理员标志位为 1 即拥有所有权限(共享页面规则)
    if (isWeeklyAdmin() || isMonthlyAdmin()) {
      return true
    }

    return userInfo.permissions?.includes(permission) || false
  } catch (error) {
    console.error('检查权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有周检管理权限
 * 周检管理员, 或 兼容老 isAdmin, 或 weeklyPermission=0(允许编辑删除)
 */
export function hasWeeklyPermission() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (isWeeklyAdmin()) return true
    return userInfo.weeklyPermission === 0 || userInfo.weeklyPermission === '0'
  } catch (error) {
    console.error('检查周检权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有延迟整改权限(周检专用)
 */
export function hasDelayedPermission() {
  return hasWeeklyPermission()
}

/**
 * 检查用户是否有月检管理权限
 * 月检管理员, 或 兼容老 isAdmin, 或 monthlyPermission=0(允许编辑删除)
 */
export function hasMonthlyPermission() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (isMonthlyAdmin()) return true
    return userInfo.monthlyPermission === 0 || userInfo.monthlyPermission === '0'
  } catch (error) {
    console.error('检查月检权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有周检提交权限
 */
export function hasSubmitPermission() {
  return hasWeeklyPermission()
}

/**
 * 检查用户是否为管理员(兼容函数,任一标志位为 1 即视为管理员)
 */
export function isAdmin() {
  return isWeeklyAdmin() || isMonthlyAdmin()
}

/**
 * 检查用户是否可以编辑用户(共享页面:任一管理员即可)
 */
export function canEditUser() {
  return isAdmin()
}

/**
 * 检查用户是否可以删除用户(共享页面:任一管理员即可)
 */
export function canDeleteUser() {
  return isAdmin()
}

/**
 * 检查用户是否可以管理用户状态(共享页面:任一管理员即可)
 */
export function canManageUserStatus() {
  return isAdmin()
}

/**
 * 检查用户是否可以重置密码(共享页面:任一管理员即可)
 */
export function canResetPassword() {
  return isAdmin()
}