package com.example.fiction_place1.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/CSS/**")
                .addResourceLocations("classpath:/static/CSS/");
        registry.addResourceHandler("/JS/**")
                .addResourceLocations("classpath:/static/JS/");
    }
}