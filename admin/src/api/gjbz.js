import request from '@/utils/request'

// 岗检标准数据API
export const gjbzDataApi = {
  // 分页查询岗检标准数据
  getPage(params) {
    return request({
      url: '/gjbz-data/page',
      method: 'get',
      params
    })
  },

  // 根据ID查询岗检标准数据
  getById(id) {
    return request({
      url: `/gjbz-data/${id}`,
      method: 'get'
    })
  },

  // 新增岗检标准数据
  add(data) {
    return request({
      url: '/gjbz-data',
      method: 'post',
      data
    })
  },

  // 更新岗检标准数据
  update(id, data) {
    return request({
      url: `/gjbz-data/${id}`,
      method: 'put',
      data
    })
  },

  // 删除岗检标准数据
  delete(id) {
    return request({
      url: `/gjbz-data/${id}`,
      method: 'delete'
    })
  },

  // 批量删除岗检标准数据
  batchDelete(ids) {
    return request({
      url: '/gjbz-data/batch',
      method: 'delete',
      data: ids
    })
  },

  // 根据类型查询数据列表
  getByType(type) {
    return request({
      url: '/gjbz-data/by-type',
      method: 'get',
      params: { type }
    })
  },

  // 获取所有下拉框选项数据
  getAllOptions() {
    return request({
      url: '/gjbz-data/options',
      method: 'get'
    })
  },

  // 获取类型选项
  getTypes() {
    return request({
      url: '/gjbz-data/options/types',
      method: 'get'
    })
  },

  // 获取模块选项
  getModules() {
    return request({
      url: '/gjbz-data/options/modules',
      method: 'get'
    })
  },

  // 获取责任单位选项
  getResponsibilities() {
    return request({
      url: '/gjbz-data/options/responsibilities',
      method: 'get'
    })
  },

  // 获取问题类型选项
  getProblemTypes() {
    return request({
      url: '/gjbz-data/options/issues',
      method: 'get'
    })
  },

  // 获取质量等级选项
  getQualityLevels() {
    return request({
      url: '/gjbz-data/quality-levels',
      method: 'get'
    })
  },

  // 根据专业类型获取模块
  getModulesByType(type) {
    return request({
      url: `/gjbz-data/modules/by-type/${type}`,
      method: 'get'
    })
  },

  // 根据模块获取责任
  getResponsibilitiesByModule(module) {
    return request({
      url: `/gjbz-data/options/responsibilities/by-module/${module}`,
      method: 'get'
    })
  },

  // 根据责任获取问题
  getIssuesByResponsibility(responsibility) {
    return request({
      url: `/gjbz-data/options/issues/by-responsibility/${responsibility}`,
      method: 'get'
    })
  }
}

export default gjbzDataApi