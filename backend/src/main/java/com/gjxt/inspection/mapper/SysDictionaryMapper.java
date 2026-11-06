package com.gjxt.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjxt.inspection.entity.SysDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典管理Mapper接口
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Mapper
public interface SysDictionaryMapper extends BaseMapper<SysDictionary> {

    /**
     * 根据字典类型查询启用状态的字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    List<SysDictionary> selectByDictType(@Param("dictType") String dictType);

    /**
     * 检查字典标签是否存在
     *
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param excludeId 排除的字典ID
     * @return 存在数量
     */
    int checkDictLabelExists(@Param("dictType") String dictType, @Param("dictLabel") String dictLabel, @Param("excludeId") String excludeId);

    /**
     * 检查字典键值是否存在
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @param excludeId 排除的字典ID
     * @return 存在数量
     */
    int checkDictValueExists(@Param("dictType") String dictType, @Param("dictValue") String dictValue, @Param("excludeId") String excludeId);

    /**
     * 获取所有字典类型
     *
     * @return 字典类型列表
     */
    List<String> selectAllDictTypes();
}