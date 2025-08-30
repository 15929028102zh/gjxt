package com.gjxt.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysDictionary;
import com.gjxt.inspection.mapper.SysDictionaryMapper;
import com.gjxt.inspection.service.ISysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典管理服务实现类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Service
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionary> implements ISysDictionaryService {

    @Autowired
    private SysDictionaryMapper dictionaryMapper;

    @Override
    public PageResult<SysDictionary> getDictionaryPage(Long current, Long size, String dictType, String dictLabel, Integer status) {
        Page<SysDictionary> page = new Page<>(current, size);
        
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(dictType), SysDictionary::getDictType, dictType)
                   .like(StringUtils.hasText(dictLabel), SysDictionary::getDictLabel, dictLabel)
                   .eq(status != null, SysDictionary::getStatus, status)
                   .eq(SysDictionary::getDeleted, 0)
                   .orderByAsc(SysDictionary::getDictType)
                   .orderByAsc(SysDictionary::getSortOrder)
                   .orderByDesc(SysDictionary::getCreateTime);
        
        IPage<SysDictionary> pageResult = this.page(page, queryWrapper);
        return PageResult.build(pageResult);
    }

    @Override
    public List<SysDictionary> getDictionaryByType(String dictType) {
        return dictionaryMapper.selectByDictType(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDictionary(SysDictionary dictionary) {
        // 检查字典标签是否已存在
        if (checkDictLabelExists(dictionary.getDictType(), dictionary.getDictLabel(), null)) {
            throw new RuntimeException("字典标签已存在");
        }
        
        // 检查字典键值是否已存在
        if (checkDictValueExists(dictionary.getDictType(), dictionary.getDictValue(), null)) {
            throw new RuntimeException("字典键值已存在");
        }
        
        // 设置创建时间
        dictionary.setCreateTime(LocalDateTime.now());
        dictionary.setUpdateTime(LocalDateTime.now());
        dictionary.setDeleted(0);
        
        // 如果没有设置状态，默认启用
        if (dictionary.getStatus() == null) {
            dictionary.setStatus(1);
        }
        
        // 如果没有设置排序，默认为0
        if (dictionary.getSortOrder() == null) {
            dictionary.setSortOrder(0);
        }
        
        return this.save(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictionary(SysDictionary dictionary) {
        // 检查字典是否存在
        SysDictionary existDictionary = this.getById(dictionary.getId());
        if (existDictionary == null || existDictionary.getDeleted() == 1) {
            throw new RuntimeException("字典不存在");
        }
        
        // 检查字典标签是否已存在（排除自己）
        String excludeId = dictionary.getId();
        if (checkDictLabelExists(dictionary.getDictType(), dictionary.getDictLabel(), excludeId)) {
            throw new RuntimeException("字典标签已存在");
        }
        
        // 检查字典键值是否已存在（排除自己）
        if (checkDictValueExists(dictionary.getDictType(), dictionary.getDictValue(), excludeId)) {
            throw new RuntimeException("字典键值已存在");
        }
        
        // 设置更新时间
        dictionary.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictionary(String id) {
        System.out.println("=== 服务层删除字典方法被调用，ID: " + id + " ===");
        // 检查字典是否存在
        SysDictionary dictionary = this.getById(id);
        if (dictionary == null || dictionary.getDeleted() == 1) {
            System.out.println("=== 字典不存在或已删除 ===");
            throw new RuntimeException("字典不存在");
        }
        
        // 物理删除
        System.out.println("=== 执行物理删除操作 ===");
        boolean result = this.removeById(id);
        System.out.println("=== 物理删除结果: " + result + " ===");
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDictionaries(List<String> ids) {
        System.out.println("=== 批量删除字典方法被调用，IDs: " + ids + " ===");
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的字典");
        }
        
        // 使用MyBatis-Plus的批量物理删除
        boolean result = this.removeByIds(ids);
        System.out.println("=== 批量物理删除结果: " + result + " ===");
        
        return result;
    }

    @Override
    public boolean checkDictLabelExists(String dictType, String dictLabel, String excludeId) {
        int count = dictionaryMapper.checkDictLabelExists(dictType, dictLabel, excludeId);
        return count > 0;
    }

    @Override
    public boolean checkDictValueExists(String dictType, String dictValue, String excludeId) {
        int count = dictionaryMapper.checkDictValueExists(dictType, dictValue, excludeId);
        return count > 0;
    }

    @Override
    public List<String> getAllDictTypes() {
        return dictionaryMapper.selectAllDictTypes();
    }

    @Override
    public Map<String, List<SysDictionary>> getAllDictionaryData() {
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionary::getStatus, 1)
                   .eq(SysDictionary::getDeleted, 0)
                   .orderByAsc(SysDictionary::getDictType)
                   .orderByAsc(SysDictionary::getSortOrder);
        
        List<SysDictionary> allDictionaries = this.list(queryWrapper);
        
        return allDictionaries.stream()
                .collect(Collectors.groupingBy(SysDictionary::getDictType));
    }
}