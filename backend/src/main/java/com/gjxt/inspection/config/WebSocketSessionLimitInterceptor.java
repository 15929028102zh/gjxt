package com.gjxt.inspection.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 会话级拦截器
 *
 * 目的:
 *  1) 单用户最多 N 条 WebSocket 连接,防止单用户开多个 tab 把 nginx worker_connections 打满
 *
 * 用法: 在 WebSocketConfig 的 registerStompEndpoints 中添加
 *      .addInterceptors(new WebSocketSessionLimitInterceptor(3))
 */
@Slf4j
public class WebSocketSessionLimitInterceptor implements HandshakeInterceptor {

    private final int maxPerUser;
    private final Map<String, CopyOnWriteArraySet<String>> userSessions = new ConcurrentHashMap<>();

    public WebSocketSessionLimitInterceptor(int maxPerUser) {
        this.maxPerUser = maxPerUser;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String userId = extractUserIdFromRequest(request);
        if (userId == null || userId.isEmpty()) {
            return true;
        }
        CopyOnWriteArraySet<String> sessions = userSessions.computeIfAbsent(
                userId, k -> new CopyOnWriteArraySet<>());
        if (sessions.size() >= maxPerUser) {
            log.warn("[WS] user={} has reached max connections {}/{}, reject handshake",
                    userId, sessions.size(), maxPerUser);
            response.setStatusCode(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
            return false;
        }
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }

    public void registerSession(String userId, String sessionId) {
        if (userId == null || sessionId == null) return;
        userSessions.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(sessionId);
    }

    public void unregisterSession(String userId, String sessionId) {
        if (userId == null || sessionId == null) return;
        CopyOnWriteArraySet<String> sessions = userSessions.get(userId);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                userSessions.remove(userId, sessions);
            }
        }
    }

    public Map<String, Integer> getCurrentStats() {
        Map<String, Integer> stats = new ConcurrentHashMap<>();
        userSessions.forEach((k, v) -> stats.put(k, v.size()));
        return stats;
    }

    private String extractUserIdFromRequest(ServerHttpRequest request) {
        if (!(request instanceof ServletServerHttpRequest)) return null;
        String auth = request.getHeaders().getFirst("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        String token = auth.substring(7);
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            int idx = payload.indexOf("\"userId\"");
            if (idx < 0) idx = payload.indexOf("\"sub\"");
            if (idx < 0) return null;
            int colon = payload.indexOf(':', idx);
            int quote1 = payload.indexOf('"', colon + 1);
            int quote2 = payload.indexOf('"', quote1 + 1);
            if (quote1 < 0 || quote2 < 0) return null;
            return payload.substring(quote1 + 1, quote2);
        } catch (Exception e) {
            return null;
        }
    }
}
