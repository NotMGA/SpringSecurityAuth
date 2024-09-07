package com.openclassrooms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // autorise toutes les routes
                        .allowedOrigins("http://localhost:4200") // origine autorisée
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // méthodes HTTP autorisées
                        .allowedHeaders("*") // tous les en-têtes autorisés
                        .allowCredentials(true); // autorise les identifiants (cookies)
            }
        };
    }
}