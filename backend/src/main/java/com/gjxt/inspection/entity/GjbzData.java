package com.gjxt.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 工程标准数据实体类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@TableName("gjbz_data")
public class GjbzData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 模块
     */
    @TableField("moudle")
    private String moudle;

    /**
     * 责任单位
     */
    @TableField("responsibility")
    private String responsibility;

    /**
     * 问题类型
     */
    @TableField("issue")
    private String issue;

    /**
     * 质量等级
     */
    @TableField("quality")
    private String quality;

    /**
     * 标准组：1=第一组(生产/炼化) 2=第二组(辅助/公用) NULL/0=通用
     */
    @TableField("standard_group")
    private Integer standardGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoudle() {
        return moudle;
    }

    public void setMoudle(String moudle) {
        this.moudle = moudle;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Integer getStandardGroup() {
        return standardGroup;
    }

    public void setStandardGroup(Integer standardGroup) {
        this.standardGroup = standardGroup;
    }
}