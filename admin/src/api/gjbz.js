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
      url: '/gjbz-data/modules',
      method: 'get'
    })
  },

  // 获取责任单位选项
  getResponsibilities() {
    return request({
      url: '/gjbz-data/responsibilities',
      method: 'get'
    })
  },

  // 获取问题类型选项
  getProblemTypes() {
    return request({
      url: '/gjbz-data/problem-types',
      method: 'get'
    })
  },

  // 获取质量等级选项
  getQualityLevels() {
    return request({
      url: '/gjbz-data/quality-levels',
      method: 'get'
    })
  }
}

export default gjbzDataApi