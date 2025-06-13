package com.org.StockEX.CORSconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;
//
//@Configuration
//public class CorsGlobalConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // allow all API paths
//                .allowedOrigins("http://localhost:4200") // allow Angular dev server
//                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS") // allowed methods
//                .allowedHeaders("*") // allow all headers
//                .allowCredentials(true); // allow cookies/session
//    }
//}


@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Allows all origins (including subdomains)
        config.addAllowedHeader("*");        // Allows all headers
        config.addAllowedMethod("*");        // Allows all HTTP methods (GET, POST, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applies to all endpoints

        return new CorsFilter(source);
    }
}