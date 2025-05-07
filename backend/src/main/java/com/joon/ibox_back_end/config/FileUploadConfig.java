package com.joon.ibox_back_end.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: backend
 * @description: 文件上传配置类
 * @author: Joonnn
 * @create: 2025-04-2917:36
 **/
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-path}")
    private String accessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(accessPath + "/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
