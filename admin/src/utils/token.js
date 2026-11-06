// Token管理工具类

/**
 * 设置token到cookie中
 * @param {string} token - 要存储的token
 * @param {number} hours - 过期时间（小时），默认2小时
 */
export function setToken(token, hours = 2) {
  const expires = new Date()
  expires.setTime(expires.getTime() + hours * 60 * 60 * 1000)
  
  // 设置cookie
  document.cookie = `token=${token}; expires=${expires.toUTCString()}; path=/; SameSite=Strict`
  
  // 同时设置到sessionStorage作为备份
  sessionStorage.setItem('token', token)
  sessionStorage.setItem('tokenExpires', expires.getTime().toString())
}

/**
 * 从cookie或sessionStorage获取token
 * @returns {string|null} token值或null
 */
export function getToken() {
  // 首先尝试从cookie获取
  const cookieToken = getCookieToken()
  if (cookieToken) {
    return cookieToken
  }
  
  // 如果cookie中没有，尝试从sessionStorage获取并检查是否过期
  const sessionToken = sessionStorage.getItem('token')
  const tokenExpires = sessionStorage.getItem('tokenExpires')
  
  if (sessionToken && tokenExpires) {
    const expiresTime = parseInt(tokenExpires)
    if (Date.now() < expiresTime) {
      return sessionToken
    } else {
      // token已过期，清除
      removeToken()
    }
  }
  
  return null
}

/**
 * 从cookie中获取token
 * @returns {string|null} token值或null
 */
function getCookieToken() {
  const cookies = document.cookie.split(';')
  for (let cookie of cookies) {
    const [name, value] = cookie.trim().split('=')
    if (name === 'token') {
      return value
    }
  }
  return null
}

/**
 * 移除token
 */
export function removeToken() {
  // 清除cookie
  document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
  
  // 清除sessionStorage
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('tokenExpires')
  
  // 清除localStorage（兼容旧版本）
  localStorage.removeItem('token')
}

/**
 * 检查token是否有效（未过期）
 * @returns {boolean} true表示有效，false表示无效或过期
 */
export function isTokenValid() {
  const token = getToken()
  return !!token
}

/**
 * 刷新token过期时间
 * @param {number} hours - 延长的小时数，默认2小时
 */
export function refreshToken(hours = 2) {
  const token = getToken()
  if (token) {
    setToken(token, hours)
  }
}