package com.gjxt.inspection.security;

import com.gjxt.inspection.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 从请求头解析 JWT，校验通过后写入 Spring Security 上下文。
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(jwtUtil.getHeader());
        if (StringUtils.hasText(header) && header.startsWith(jwtUtil.getPrefix())) {
            String token = jwtUtil.getTokenFromHeader(header);
            try {
                Claims claims = jwtUtil.getClaimFromToken(token, c -> c);
                if (claims != null && !jwtUtil.isTokenExpired(token)) {
                    String userId = claims.get("userId") != null ? claims.get("userId").toString() : null;
                    String username = claims.getSubject();
                    String role = claims.get("role") != null ? claims.get("role").toString() : "user";
                    Integer isAdmin = claims.get("isAdmin") != null
                            ? Integer.valueOf(claims.get("isAdmin").toString()) : 0;
                    Integer isWeeklyAdmin = claims.get("isWeeklyAdmin") != null
                            ? Integer.valueOf(claims.get("isWeeklyAdmin").toString()) : isAdmin;
                    Integer isMonthlyAdmin = claims.get("isMonthlyAdmin") != null
                            ? Integer.valueOf(claims.get("isMonthlyAdmin").toString()) : 0;

                    JwtPrincipal principal = new JwtPrincipal(userId, username, role, isWeeklyAdmin, isMonthlyAdmin);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            principal, token,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                log.debug("JWT 解析失败: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}