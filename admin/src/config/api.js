// API配置文件
// 统一管理所有API相关的URL配置

// 获取API基础URL
// API配置管理
// 获取API基础URL
export const getApiBaseUrl = () => {
  // 在开发环境下，使用Vite代理，直接返回相对路径
  if (import.meta.env.DEV) {
    return ''
  }
  
  // 在生产环境下，使用环境变量配置的URL
  return import.meta.env.VITE_API_BASE_URL || 'http://10.116.40.104:3000'
}

// 获取完整的API URL
export const getFullApiUrl = (path = '') => {
  const baseUrl = getApiBaseUrl()
  
  // 开发环境：baseUrl为空字符串，直接返回path
  if (!baseUrl) {
    return path
  }
  
  // 确保path以/开头
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  
  // 生产环境：拼接完整URL，避免重复的斜杠
  if (baseUrl.endsWith('/')) {
    return `${baseUrl.slice(0, -1)}${normalizedPath}`
  }
  
  return `${baseUrl}${normalizedPath}`
}

// 解析图片URL的工具函数
export const parseImageUrls = (imageUrls) => {
  if (!imageUrls) return []
  
  try {
    let urlList = []
    if (typeof imageUrls === 'string') {
      // 首先尝试JSON解析
      try {
        const parsed = JSON.parse(imageUrls)
        if (Array.isArray(parsed)) {
          urlList = parsed
        }
      } catch (e) {
        // JSON解析失败，尝试逗号分割
        urlList = imageUrls.split(',').map(url => url.trim()).filter(url => url)
      }
    } else if (Array.isArray(imageUrls)) {
      urlList = imageUrls
    }
    
    // 转换为完整URL
    return urlList.map(url => convertToHttpUrl(url)).filter(url => url)
  } catch (e) {
    console.error('解析图片URL失败:', e, '原始数据:', imageUrls)
    return []
  }
}

// 获取文件上传的URL
export const getUploadUrl = () => {
  return getFullApiUrl('/api/files/upload')
}

// 获取图片上传的URL
export const getImageUploadUrl = () => {
  return getFullApiUrl('/api/upload/image')
}

// 将相对路径转换为HTTP访问路径
export const convertToHttpUrl = (relativePath) => {
  if (!relativePath || relativePath.trim() === '') {
    return ''
  }
  
  // 如果已经是完整的HTTP/HTTPS URL，直接返回
  if (relativePath.startsWith('http://') || relativePath.startsWith('https://')) {
    return relativePath
  }
  
  // 使用统一的API配置来拼接完整的HTTP URL
  // 后端返回相对路径，如: /files/images/20250831/xxx.jpg
  return getFullApiUrl(relativePath)
}

export default {
  getApiBaseUrl,
  getFullApiUrl,
  parseImageUrls,
  getUploadUrl,
  getImageUploadUrl,
  convertToHttpUrl
}