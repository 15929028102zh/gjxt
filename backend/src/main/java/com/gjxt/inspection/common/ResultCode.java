package com.gjxt.inspection.common;

/**
 * 响应码枚举
 *
 * @author gjxt
 * @since 2024-01-01
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1001, "用户名或密码错误"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(1002, "用户已被禁用"),

    /**
     * Token无效
     */
    TOKEN_INVALID(1003, "Token无效"),

    /**
     * Token已过期
     */
    TOKEN_EXPIRED(1004, "Token已过期"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1005, "用户不存在"),

    /**
     * 用户名已存在
     */
    USERNAME_EXISTS(1006, "用户名已存在"),

    /**
     * 数据已存在
     */
    DATA_EXISTS(2001, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(2002, "数据不存在"),

    /**
     * 数据正在使用中
     */
    DATA_IN_USE(2003, "数据正在使用中，无法删除"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(3001, "文件上传失败"),

    /**
     * 文件类型不支持
     */
    FILE_TYPE_ERROR(3002, "文件类型不支持"),

    /**
     * 文件大小超限
     */
    FILE_SIZE_ERROR(3003, "文件大小超限"),

    /**
     * 任务不存在
     */
    TASK_NOT_FOUND(4001, "巡检任务不存在"),

    /**
     * 任务已完成
     */
    TASK_COMPLETED(4002, "巡检任务已完成"),

    /**
     * 记录不存在
     */
    RECORD_NOT_FOUND(4003, "巡检记录不存在"),

    /**
     * 记录状态错误
     */
    RECORD_STATUS_ERROR(4004, "巡检记录状态错误"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(4005, "权限不足");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}