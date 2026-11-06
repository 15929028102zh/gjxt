package com.gjxt.inspection.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("石油工业园区设备巡检系统API文档")
                        .description("基于Spring Boot + MyBatis + MySQL的设备巡检管理系统")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("gjxt")
                                .email("")
                                .url("")));
    }
}