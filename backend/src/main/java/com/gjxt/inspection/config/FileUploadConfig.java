package com.gjxt.inspection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件上传配置
 *
 * @author gjxt
 * @since 2024-01-01
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${file.upload.access-path}")
    private String accessPath;

    @Value("${file.upload.real-path}")
    private String realPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件访问路径
        registry.addResourceHandler(accessPath)
                .addResourceLocations("file:" + realPath);
    }

}