/**
 * 权限工具函数
 */

/**
 * 检查用户是否有指定权限
 * @param {string} permission 权限标识
 * @returns {boolean} 是否有权限
 */
export function hasPermission(permission) {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    // 管理员拥有所有权限
    if (userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1') {
      return true
    }
    
    // 检查用户权限列表
    return userInfo.permissions?.includes(permission) || false
  } catch (error) {
    console.error('检查权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有周检管理权限
 * @returns {boolean} 是否有权限
 */
export function hasWeeklyPermission() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    // 管理员拥有所有权限
    if (userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1') {
      return true
    }
    
    // 检查周检权限字段：0=允许编辑删除，1=禁用编辑删除
    return userInfo.weeklyPermission === 0 || userInfo.weeklyPermission === '0'
  } catch (error) {
    console.error('检查周检权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有延迟整改权限
 * @returns {boolean} 是否有权限
 */
export function hasDelayedPermission() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    // 管理员拥有所有权限
    if (userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1') {
      return true
    }
    
    // 检查周检权限字段：0=允许编辑删除，1=禁用编辑删除
    return userInfo.weeklyPermission === 0 || userInfo.weeklyPermission === '0'
  } catch (error) {
    console.error('检查延迟整改权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有月检管理权限
 * @returns {boolean} 是否有权限
 */
export function hasMonthlyPermission() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    // 管理员拥有所有权限
    if (userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1') {
      return true
    }
    
    // 检查月检权限字段：0=允许编辑删除，1=禁用编辑删除
    return userInfo.monthlyPermission === 0 || userInfo.monthlyPermission === '0'
  } catch (error) {
    console.error('检查月检权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否有周检提交权限
 * @returns {boolean} 是否有权限
 */
export function hasSubmitPermission() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    // 管理员拥有所有权限
    if (userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1') {
      return true
    }
    
    // 检查周检权限字段：0=允许编辑删除，1=禁用编辑删除
    return userInfo.weeklyPermission === 0 || userInfo.weeklyPermission === '0'
  } catch (error) {
    console.error('检查周检提交权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否为管理员
 * @returns {boolean} 是否为管理员
 */
export function isAdmin() {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    // 检查isAdmin字段：1=是管理员，0=不是管理员
    return userInfo.isAdmin === 1 || userInfo.isAdmin === true || userInfo.isAdmin === '1'
  } catch (error) {
    console.error('检查管理员权限失败:', error)
    return false
  }
}

/**
 * 检查用户是否可以编辑用户
 * @returns {boolean} 是否有权限
 */
export function canEditUser() {
  return isAdmin() || hasPermission('system:user:edit')
}

/**
 * 检查用户是否可以删除用户
 * @returns {boolean} 是否有权限
 */
export function canDeleteUser() {
  return isAdmin() || hasPermission('system:user:delete')
}

/**
 * 检查用户是否可以管理用户状态
 * @returns {boolean} 是否有权限
 */
export function canManageUserStatus() {
  return isAdmin() || hasPermission('system:user:status')
}

/**
 * 检查用户是否可以重置密码
 * @returns {boolean} 是否有权限
 */
export function canResetPassword() {
  return isAdmin() || hasPermission('system:user:resetPassword')
}