package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjxt.inspection.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 查询所有部门列表
     *
     * @return 部门列表
     */
    List<SysDept> selectDeptList();

    /**
     * 根据父部门ID查询子部门
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<SysDept> selectByParentId(@Param("parentId") String parentId);

    /**
     * 检查部门名称是否存在
     *
     * @param deptName 部门名称
     * @param excludeId 排除的部门ID
     * @return 存在数量
     */
    int checkDeptNameExists(@Param("deptName") String deptName, @Param("excludeId") String excludeId);

    /**
     * 检查部门是否有子部门
     *
     * @param deptId 部门ID
     * @return 子部门数量
     */
    int hasChildByDeptId(@Param("deptId") String deptId);
}