package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysDept;

import java.util.List;

/**
 * 部门服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 获取部门树形结构
     *
     * @return 部门树形列表
     */
    List<SysDept> getDeptTree();

    /**
     * 分页查询部门列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @param deptName 部门名称（模糊查询）
     * @param status 状态
     * @return 部门分页列表
     */
    PageResult<SysDept> getDeptPage(Long current, Long size, String deptName, Integer status);

    /**
     * 新增部门
     *
     * @param dept 部门信息
     * @return 是否成功
     */
    boolean addDept(SysDept dept);

    /**
     * 更新部门
     *
     * @param dept 部门信息
     * @return 是否成功
     */
    boolean updateDept(SysDept dept);

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 是否成功
     */
    boolean deleteDept(String id);

    /**
     * 批量删除部门
     *
     * @param ids 部门ID列表
     * @return 是否成功
     */
    boolean batchDeleteDepts(List<String> ids);

    /**
     * 更新部门状态
     *
     * @param id 部门ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateDeptStatus(Long id, Integer status);

    /**
     * 检查部门名称是否存在
     *
     * @param deptName 部门名称
     * @param excludeId 排除的部门ID
     * @return 是否存在
     */
    boolean checkDeptNameExists(String deptName, Long excludeId);

    /**
     * 检查是否有子部门
     *
     * @param deptId 部门ID
     * @return 是否有子部门
     */
    boolean hasChildDept(Long deptId);

    /**
     * 获取启用的部门列表
     *
     * @return 启用的部门列表
     */
    List<SysDept> getEnabledDeptList();

}