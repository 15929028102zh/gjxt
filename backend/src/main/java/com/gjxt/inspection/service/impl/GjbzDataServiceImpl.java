package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.GjbzData;
import com.gjxt.inspection.mapper.GjbzDataMapper;
import com.gjxt.inspection.service.IGjbzDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工程标准数据服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Service
public class GjbzDataServiceImpl extends ServiceImpl<GjbzDataMapper, GjbzData> implements IGjbzDataService {

    @Autowired
    private GjbzDataMapper gjbzDataMapper;

    @Override
    public PageResult<GjbzData> getGjbzDataPage(Long current, Long size, String type) {
        Page<GjbzData> page = new Page<>(current, size);
        QueryWrapper<GjbzData> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(type)) {
            queryWrapper.eq("type", type);
        }
        
        queryWrapper.orderByAsc("id");
        IPage<GjbzData> result = gjbzDataMapper.selectPage(page, queryWrapper);
        
        return new PageResult<GjbzData>(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    @Override
    public List<GjbzData> getDataByType(String type) {
        return gjbzDataMapper.selectByType(type);
    }

    @Override
    public List<GjbzData> getDataByProfession(String profession) {
        QueryWrapper<GjbzData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("type", profession)
                   .or()
                   .like("moudle", profession)
                   .or()
                   .like("responsibility", profession);
        queryWrapper.orderByAsc("id");
        return gjbzDataMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, List<String>> getAllOptions() {
        Map<String, List<String>> options = new HashMap<>();
        options.put("modules", getModuleOptions());
        options.put("responsibilities", getResponsibilityOptions());
        options.put("issues", getIssueOptions());
        options.put("qualities", getQualityOptions());
        options.put("types", getTypeOptions());
        return options;
    }

    @Override
    public List<String> getModuleOptions() {
        return gjbzDataMapper.selectAllModules();
    }

    @Override
    public List<String> getModulesByType(String type) {
        return gjbzDataMapper.selectModulesByType(type);
    }

    @Override
    public List<String> getModulesByType(String type, Integer standardGroup) {
        if (standardGroup == null) {
            return gjbzDataMapper.selectModulesByType(type);
        }
        return gjbzDataMapper.selectModulesByTypeAndGroup(type, standardGroup);
    }

    @Override
    public List<String> getResponsibilityOptions() {
        return gjbzDataMapper.selectAllResponsibilities();
    }

    @Override
    public List<String> getResponsibilitiesByModule(String module) {
        return gjbzDataMapper.selectResponsibilitiesByModule(module, null);
    }

    @Override
    public List<String> getResponsibilitiesByModule(String module, Integer standardGroup, String type) {
        if (standardGroup == null) {
            return gjbzDataMapper.selectResponsibilitiesByModule(module, type);
        }
        return gjbzDataMapper.selectResponsibilitiesByModuleAndGroup(module, standardGroup, type);
    }

    @Override
    public List<String> getIssueOptions() {
        return gjbzDataMapper.selectAllIssues();
    }

    @Override
    public List<String> getIssuesByResponsibility(String responsibility) {
        return gjbzDataMapper.selectIssuesByResponsibility(responsibility, null, null);
    }

    @Override
    public List<String> getIssuesByResponsibility(String responsibility, Integer standardGroup, String type, String module) {
        if (standardGroup == null) {
            return gjbzDataMapper.selectIssuesByResponsibility(responsibility, type, module);
        }
        return gjbzDataMapper.selectIssuesByResponsibilityAndGroup(responsibility, standardGroup, type, module);
    }

    @Override
    public List<GjbzData> getIssueOptionsWithQuality() {
        return gjbzDataMapper.selectAllIssuesWithQuality();
    }

    @Override
    public List<GjbzData> getIssueOptionsWithQuality(Integer standardGroup) {
        if (standardGroup == null) {
            return gjbzDataMapper.selectAllIssuesWithQuality();
        }
        return gjbzDataMapper.selectAllIssuesWithQualityByGroup(standardGroup);
    }

    @Override
    public List<String> getQualityOptions() {
        return gjbzDataMapper.selectAllQualities();
    }

    @Override
    public List<String> getTypeOptions() {
        return gjbzDataMapper.selectAllTypes();
    }

    @Override
    public List<String> getTypeOptions(Integer standardGroup) {
        if (standardGroup == null) {
            return gjbzDataMapper.selectAllTypes();
        }
        return gjbzDataMapper.selectAllTypesByGroup(standardGroup);
    }

    @Override
    public boolean addGjbzData(GjbzData gjbzData) {
        return gjbzDataMapper.insert(gjbzData) > 0;
    }

    @Override
    public boolean updateGjbzData(GjbzData gjbzData) {
        return gjbzDataMapper.updateById(gjbzData) > 0;
    }

    @Override
    public boolean deleteGjbzData(Integer id) {
        return gjbzDataMapper.deleteById(id) > 0;
    }

    @Override
    public boolean batchDeleteGjbzData(List<Integer> ids) {
        return gjbzDataMapper.deleteBatchIds(ids) > 0;
    }
}