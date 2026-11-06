package com.gjxt.inspection.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * JWT 解析后的主体信息
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Data
@AllArgsConstructor
public class JwtPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String role;
    private Integer isWeeklyAdmin;
    private Integer isMonthlyAdmin;

    /**
     * 兼容:任一管理员标志位为 1 即视为管理员
     */
    public boolean isAdmin() {
        return isWeeklyAdmin() || isMonthlyAdmin();
    }

    public boolean isWeeklyAdmin() {
        return isWeeklyAdmin != null && isWeeklyAdmin == 1;
    }

    public boolean isMonthlyAdmin() {
        return isMonthlyAdmin != null && isMonthlyAdmin == 1;
    }
}