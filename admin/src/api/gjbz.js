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
  // departmentId(可选): 受检单位部门ID, 后端据此按标准组过滤(第一组生产/第二组辅助/全部)
  getTypes(departmentId) {
    return request({
      url: '/gjbz-data/options/types',
      method: 'get',
      params: departmentId ? { departmentId } : {}
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
  // departmentId(可选): 受检单位部门ID, 后端据此按标准组过滤
  getModulesByType(type, departmentId) {
    return request({
      url: `/gjbz-data/modules/by-type/${encodeURIComponent(type)}`,
      method: 'get',
      params: departmentId ? { departmentId } : {}
    })
  },

  // 根据模块获取责任
  // departmentId(可选): 受检单位部门ID, 后端据此按标准组过滤
  // type(可选): 专业类别名称, 后端据此限定责任要素归属, 避免同名模块跨专业类别串味
  // module 走 query 参数而非路径: 路径参数遇到含 "/" 的名称会被拆成多级路径
  getResponsibilitiesByModule(module, departmentId, type) {
    return request({
      url: `/gjbz-data/options/responsibilities/by-module`,
      method: 'get',
      params: {
        module,
        ...(departmentId ? { departmentId } : {}),
        ...(type ? { type } : {})
      }
    })
  },

  // 根据责任获取问题
  // departmentId(可选): 受检单位部门ID, 后端据此按标准组过滤
  // type/module(可选): 专业类别与模块名称, 后端据此限定检查内容归属, 避免同名责任要素跨模块串味
  // responsibility 走 query 参数而非路径: 存在含 "/" 的责任要素名(如 盲板抽/堵作业要求),
  // 放路径里会被拆成多级路径导致 404
  getIssuesByResponsibility(responsibility, departmentId, type, module) {
    return request({
      url: `/gjbz-data/options/issues/by-responsibility`,
      method: 'get',
      params: {
        responsibility,
        ...(departmentId ? { departmentId } : {}),
        ...(type ? { type } : {}),
        ...(module ? { module } : {})
      }
    })
  },

  // 获取问题选项及对应性质界定映射（用于选择检查内容后自动带出性质界定）
  // departmentId(可选): 受检单位部门ID, 后端据此按标准组过滤
  getIssueOptionsWithQuality(departmentId) {
    return request({
      url: '/gjbz-data/options/issues-with-quality',
      method: 'get',
      params: departmentId ? { departmentId } : {}
    })
  }
}

export default gjbzDataApi