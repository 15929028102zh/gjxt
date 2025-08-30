package com.gjxt.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gjxt.inspection.common.PageResult;
import com.gjxt.inspection.entity.SysDictionary;

import java.util.List;
import java.util.Map;

/**
 * 字典管理服务接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
public interface ISysDictionaryService extends IService<SysDictionary> {

    /**
     * 分页查询字典列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param status 状态
     * @return 分页结果
     */
    PageResult<SysDictionary> getDictionaryPage(Long current, Long size, String dictType, String dictLabel, Integer status);

    /**
     * 根据字典类型查询启用状态的字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    List<SysDictionary> getDictionaryByType(String dictType);

    /**
     * 新增字典
     *
     * @param dictionary 字典信息
     * @return 是否成功
     */
    boolean addDictionary(SysDictionary dictionary);

    /**
     * 更新字典
     *
     * @param dictionary 字典信息
     * @return 是否成功
     */
    boolean updateDictionary(SysDictionary dictionary);

    /**
     * 删除字典
     *
     * @param id 字典ID
     * @return 是否成功
     */
    boolean deleteDictionary(String id);

    /**
     * 批量删除字典
     *
     * @param ids 字典ID列表
     * @return 是否成功
     */
    boolean batchDeleteDictionaries(List<String> ids);

    /**
     * 检查字典标签是否存在
     *
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param excludeId 排除的字典ID
     * @return 是否存在
     */
    boolean checkDictLabelExists(String dictType, String dictLabel, String excludeId);

    /**
     * 检查字典键值是否存在
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @param excludeId 排除的字典ID
     * @return 是否存在
     */
    boolean checkDictValueExists(String dictType, String dictValue, String excludeId);

    /**
     * 获取所有字典类型
     *
     * @return 字典类型列表
     */
    List<String> getAllDictTypes();

    /**
     * 获取字典数据（按类型分组）
     *
     * @return 字典数据Map
     */
    Map<String, List<SysDictionary>> getAllDictionaryData();
}