/**
 * 权限工具（与 Web 端 admin/src/utils/permission.js 保持一致）
 *
 * V3 规则：is_admin 已废弃，拆分为 isWeeklyAdmin / isMonthlyAdmin。
 *  - 周检/巡检域管理员：isWeeklyAdmin() = 老 isAdmin===1（读兼容） 或 isWeeklyAdmin===1
 *  - 月检域管理员：isMonthlyAdmin() = 严格只看 isMonthlyAdmin===1
 *    （周检管理员 / 老 isAdmin=1 不再视为月检管理员）
 *  - 编辑删除权限：hasWeeklyPermission() / hasMonthlyPermission()
 *    = 对应域管理员 或 xxxPermission===0（0=允许编辑删除，1=禁用）
 *
 * 数据来源：登录后 uni.setStorageSync('userInfo', ...) 存储的 SysUser 实体 JSON，
 * 后端 /user/info 直接返回完整 SysUser，故含 isMonthlyAdmin/isWeeklyAdmin/
 * monthlyPermission/weeklyPermission/deptId 等字段。
 */

// 读取本地 userInfo（解析失败或不存在时返回空对象）
function readUserInfo(): UTSJSONObject {
  const raw = uni.getStorageSync('userInfo')
  const str = raw != null ? `${raw}` : ''
  if (str == '' || str == 'null' || str == 'undefined') {
    return {} as UTSJSONObject
  }
  try {
    return JSON.parse(str) as UTSJSONObject
  } catch (e) {
    return {} as UTSJSONObject
  }
}

// 读取整型标志位，兼容 number(1) / string('1') / boolean(true) 三种存储形态
function readFlag(info: UTSJSONObject, key: string): boolean {
  const n = info.getNumber(key)
  if (n != null) return n === 1
  const s = info.getString(key)
  if (s != null) return s === '1' || s === 'true'
  const b = info.getBoolean(key)
  if (b != null) return b
  return false
}

// 读取权限位是否为 0（0=允许编辑删除）；字段缺失视为 false（与 Web 端 undefined!==0 一致）
function readPermissionZero(info: UTSJSONObject, key: string): boolean {
  const n = info.getNumber(key)
  if (n != null) return n === 0
  const s = info.getString(key)
  if (s != null) return s === '0'
  return false
}

/**
 * 是否为周检管理员（兼容老 isAdmin=1）
 */
export function isWeeklyAdmin(): boolean {
  const info = readUserInfo()
  if (readFlag(info, 'isAdmin')) return true
  return readFlag(info, 'isWeeklyAdmin')
}

/**
 * 是否为月检管理员（V3 严格：只看 isMonthlyAdmin=1）
 */
export function isMonthlyAdmin(): boolean {
  const info = readUserInfo()
  return readFlag(info, 'isMonthlyAdmin')
}

/**
 * 是否为管理员（兼容函数：任一周检/月检管理员标志位为 1 即视为管理员）
 */
export function isAdmin(): boolean {
  return isWeeklyAdmin() || isMonthlyAdmin()
}

/**
 * 是否有周检管理权限（周检管理员，或 weeklyPermission===0 允许编辑删除）
 */
export function hasWeeklyPermission(): boolean {
  if (isWeeklyAdmin()) return true
  const info = readUserInfo()
  return readPermissionZero(info, 'weeklyPermission')
}

/**
 * 是否有月检管理权限（月检管理员，或 monthlyPermission===0 允许编辑删除）
 */
export function hasMonthlyPermission(): boolean {
  if (isMonthlyAdmin()) return true
  const info = readUserInfo()
  return readPermissionZero(info, 'monthlyPermission')
}

/**
 * 获取当前用户部门 ID（无则返回空串）
 */
export function getUserDeptId(): string {
  const info = readUserInfo()
  return info.getString('deptId') ?? ''
}
