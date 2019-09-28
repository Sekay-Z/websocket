package com.shukai.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CorsConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("Content-Type","Content-Length", "Authorization", "Accept","X-Requested-With")
                .allowedMethods("PUT","POST","GET","DELETE","OPTIONS")
                .allowedOrigins("http://localhost:8090");
    }
}
