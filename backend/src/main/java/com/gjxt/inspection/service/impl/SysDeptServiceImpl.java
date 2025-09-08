package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysDept;
import com.gjxt.inspection.mapper.SysDeptMapper;
import com.gjxt.inspection.service.ISysDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<SysDept> getDeptTree() {
        // 查询所有部门
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getStatus, 1)
                .orderByAsc(SysDept::getSortOrder, SysDept::getId);
        List<SysDept> allDepts = baseMapper.selectList(wrapper);
        
        // 构建树形结构
        return buildDeptTree(allDepts, null);
    }

    @Override
    public PageResult<SysDept> getDeptPage(Long current, Long size, String deptName, Integer status) {
        Page<SysDept> page = new Page<>(current, size);
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(deptName)) {
            wrapper.like(SysDept::getDeptName, deptName);
        }
        if (status != null) {
            wrapper.eq(SysDept::getStatus, status);
        }
        
        wrapper.orderByAsc(SysDept::getSortOrder, SysDept::getId);
        
        IPage<SysDept> pageResult = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(pageResult.getCurrent(), pageResult.getSize(), 
                               pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDept(SysDept dept) {
        // 检查部门名称是否重复
        if (checkDeptNameExists(dept.getDeptName(), null)) {
            throw new RuntimeException("部门名称已存在");
        }
        
        // 设置默认值
        if (dept.getStatus() == null) {
            dept.setStatus(1);
        }
        if (dept.getSortOrder() == null) {
            dept.setSortOrder(0);
        }
        if (dept.getParentId() == null) {
            dept.setParentId("0");
        }
        
        // 设置时间字段
        LocalDateTime now = LocalDateTime.now();
        dept.setCreateTime(now);
        dept.setUpdateTime(now);
        
        return save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        // 检查部门名称是否重复
        if (checkDeptNameExists(dept.getDeptName(), dept.getId())) {
            throw new RuntimeException("部门名称已存在");
        }
        
        // 设置更新时间
        dept.setUpdateTime(LocalDateTime.now());
        
        return updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(String id) {
        // 检查是否有子部门
        if (hasChildDept(id) ){
            throw new RuntimeException("存在子部门，无法删除");
        }
        
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDepts(List<String> ids) {
        // 检查每个部门是否有子部门
        for (String id : ids) {
            if (hasChildDept(id)) {
                throw new RuntimeException("存在子部门，无法删除");
            }
        }
        
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptStatus(String id, Integer status) {
        SysDept dept = new SysDept();
        dept.setId(id);
        dept.setStatus(status);
        dept.setUpdateTime(LocalDateTime.now());
        return updateById(dept);
    }

    @Override
    public boolean checkDeptNameExists(String deptName, String excludeId) {
        int count = baseMapper.checkDeptNameExists(deptName, excludeId);
        return count > 0;
    }

    @Override
    public boolean hasChildDept(String deptId) {
        int count = baseMapper.hasChildByDeptId(deptId);
        return count > 0;
    }

    @Override
    public List<SysDept> getEnabledDeptList() {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getStatus, 1)
                   .eq(SysDept::getDeleted, 0)
                   .orderByAsc(SysDept::getSortOrder);
        return this.list(queryWrapper);
    }

    /**
     * 构建部门树形结构
     *
     * @param allDepts 所有部门列表
     * @param parentId 父部门ID
     * @return 树形结构列表
     */
    private List<SysDept> buildDeptTree(List<SysDept> allDepts, String parentId) {
        List<SysDept> result = new ArrayList<>();
        for (SysDept dept : allDepts) {
            String deptParentId = dept.getParentId();
            if ((parentId == null && (deptParentId == null || "0".equals(deptParentId))) ||
                (parentId != null && parentId.equals(deptParentId))) {
                
                // 递归查找子部门
                List<SysDept> children = buildDeptTree(allDepts, dept.getId());
                // 这里可以设置children属性，如果SysDept实体类有children字段的话
                
                result.add(dept);
            }
        }
        
        return result;
    }
}