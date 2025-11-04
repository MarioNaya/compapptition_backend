package com.compapptition.backend.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    /**
     * Configuración CORS para permitir peticiones desde el frontend
     * Útil cuando frontend y backend están en diferentes dominios/puertos
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  // Aplica a todos los endpoints /api/*

                        // Orígenes permitidos (tu frontend)
                        .allowedOrigins(
                                "http://localhost:3000",      // React/Vue/Angular dev
                                "http://localhost:4200",      // Angular dev
                                "http://localhost:8081",      // Otro puerto
                                "http://localhost",           // XAMPP sin puerto
                                "http://localhost:80"         // XAMPP con puerto explícito
                        )

                        // Métodos HTTP permitidos
                        .allowedMethods(
                                "GET",
                                "POST",
                                "PUT",
                                "DELETE",
                                "PATCH",
                                "OPTIONS"
                        )

                        // Headers permitidos
                        .allowedHeaders("*")

                        // Permitir credenciales (cookies, auth headers)
                        .allowCredentials(true)

                        // Tiempo de caché de preflight (OPTIONS)
                        .maxAge(3600);
            }
        };
    }

    /**
     * Configuración CORS alternativa (más detallada)
     * Descomenta si necesitas más control
     */
    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Métodos permitidos
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        );

        // Headers permitidos
        configuration.setAllowedHeaders(
            Arrays.asList("Authorization", "Content-Type", "X-Requested-With")
        );

        // Headers expuestos al cliente
        configuration.setExposedHeaders(
            Arrays.asList("Authorization", "X-Total-Count")
        );

        // Permitir credenciales
        configuration.setAllowCredentials(true);

        // Tiempo de caché
        configuration.setMaxAge(3600L);

        // Aplicar a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    */
}