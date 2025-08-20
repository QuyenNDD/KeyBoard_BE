package com.project.keyboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Cho phép mọi domain/IP
                .allowedMethods("*")        // Cho phép mọi method (GET, POST, PUT, DELETE...)
                .allowedHeaders("*")        // Cho phép mọi header
                .allowCredentials(true);    // Cho phép gửi cookie/authorization
    }
}
