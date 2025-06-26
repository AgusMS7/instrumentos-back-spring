package com.instrumentos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS para APIs
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                    "http://localhost:*", 
                    "http://127.0.0.1:*",
                    "https://localhost:*", 
                    "https://127.0.0.1:*"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
                
        // CORS para imágenes
        registry.addMapping("/images/**")
                .allowedOriginPatterns(
                    "http://localhost:*", 
                    "http://127.0.0.1:*",
                    "https://localhost:*", 
                    "https://127.0.0.1:*"
                )
                .allowedMethods("GET", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(86400); // Cache por 24 horas
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir imágenes desde el directorio public/images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:public/images/", "classpath:/static/images/")
                .setCachePeriod(3600) // Cache por 1 hora
                .resourceChain(true);
    }
}
