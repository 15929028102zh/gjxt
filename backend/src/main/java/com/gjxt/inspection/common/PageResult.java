package com.gjxt.inspection.common;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果类
 *
 * @author gjxt
 * @since 2024-01-01
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    public PageResult() {
    }

    public PageResult(Long current, Long size, Long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
        this.pages = (total + size - 1) / size;
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> build(Long current, Long size, Long total, List<T> records) {
        return new PageResult<>(current, size, total, records);
    }

    /**
     * 从MyBatis-Plus的IPage构建分页结果
     */
    public static <T> PageResult<T> build(com.baomidou.mybatisplus.core.metadata.IPage<T> page) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    /**
     * 构建空的分页结果
     */
    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(current, size, 0L, null);
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}