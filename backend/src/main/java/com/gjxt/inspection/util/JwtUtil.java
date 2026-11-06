package com.gjxt.inspection.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    /**
     * 生成JWT Token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     * @return JWT Token
     */
    public String generateToken(String userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return createToken(claims, username);
    }

    /**
     * 生成JWT Token（包含管理员信息）
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     * @param isAdmin 是否为管理员（已废弃,新代码请用 5 参数重载）
     * @return JWT Token
     */
    @Deprecated
    public String generateToken(String userId, String username, String role, Integer isAdmin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        claims.put("isAdmin", isAdmin);
        return createToken(claims, username);
    }

    /**
     * 生成JWT Token（周检/月检管理员独立标识）
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     * @param isWeeklyAdmin 周检管理员 0/1
     * @param isMonthlyAdmin 月检管理员 0/1
     * @return JWT Token
     */
    public String generateToken(String userId, String username, String role, Integer isWeeklyAdmin, Integer isMonthlyAdmin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        claims.put("isWeeklyAdmin", isWeeklyAdmin);
        claims.put("isMonthlyAdmin", isMonthlyAdmin);
        // 旧 isAdmin claim 兼容(任一为 1 视为 1),保证老 token 仍可被识别为管理员
        Integer isAdmin = (isWeeklyAdmin != null && isWeeklyAdmin == 1) || (isMonthlyAdmin != null && isMonthlyAdmin == 1) ? 1 : 0;
        claims.put("isAdmin", isAdmin);
        return createToken(claims, username);
    }

    /**
     * 创建Token
     *
     * @param claims 声明
     * @param subject 主题
     * @return Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从Token中获取用户名
     *
     * @param token Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token Token
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId").toString();
    }

    /**
     * 从Token中获取用户角色
     *
     * @param token Token
     * @return 用户角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role").toString();
    }

    /**
     * 从Token中获取管理员标识
     *
     * @param token Token
     * @return 是否为管理员
     */
    public Integer getIsAdminFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object isAdmin = claims.get("isAdmin");
        return isAdmin != null ? Integer.valueOf(isAdmin.toString()) : 0;
    }

    /**
     * 从Token中获取周检管理员标识
     *
     * @param token Token
     * @return 周检管理员 0/1
     */
    public Integer getIsWeeklyAdminFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object isWeeklyAdmin = claims.get("isWeeklyAdmin");
        return isWeeklyAdmin != null ? Integer.valueOf(isWeeklyAdmin.toString()) : 0;
    }

    /**
     * 从Token中获取月检管理员标识
     *
     * @param token Token
     * @return 月检管理员 0/1
     */
    public Integer getIsMonthlyAdminFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object isMonthlyAdmin = claims.get("isMonthlyAdmin");
        return isMonthlyAdmin != null ? Integer.valueOf(isMonthlyAdmin.toString()) : 0;
    }

    /**
     * 从Token中获取过期时�?     *
     * @param token Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从Token中获取指定声�?     *
     * @param token Token
     * @param claimsResolver 声明解析�?     * @param <T> 返回类型
     * @return 声明�?     */
    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从Token中获取所有声�?     *
     * @param token Token
     * @return 所有声�?     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查Token是否过期
     *
     * @param token Token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 验证Token
     *
     * @param token Token
     * @param username 用户�?     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    /**
     * 刷新Token
     *
     * @param token 原Token
     * @return 新Token
     */
    public String refreshToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(new Date());
        return createToken(claims, claims.getSubject());
    }

    /**
     * 从请求头中获取Token
     *
     * @param authHeader 授权�?     * @return Token
     */
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(prefix)) {
            return authHeader.substring(prefix.length());
        }
        return null;
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

}