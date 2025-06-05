package com.org.StockEX.CORSconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // allow all API paths
                .allowedOrigins("http://localhost:4200") // allow Angular dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS") // allowed methods
                .allowedHeaders("*") // allow all headers
                .allowCredentials(true); // allow cookies/session
    }
}
