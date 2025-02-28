package com.example.ems.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply to all endpoints under /api
                .allowedOrigins("http://localhostgi ") // Your React app's URL
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Allow these methods
        //.allowedHeaders("*"); // Optional: Allow all headers (use with caution in production)
    }
}