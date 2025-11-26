package com.gjxt.inspection.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * 用于配置WebSocket消息代理和端点
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单消息代理，并设置消息代理的前缀
        config.enableSimpleBroker("/topic", "/queue");
        // 设置应用程序的目标前缀
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户目标前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点，并允许跨域
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}