package calculator.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to set up CORS (Cross-Origin Resource Sharing) for the REST API.
 * This allows clients from different origins (e.g., a frontend application running on a different port) to access the API
 * without being blocked by the browser's same-origin policy.
 * In this configuration, we allow all origins to access any endpoint under "/api/**". In a production environment,
 * you might want to restrict this to specific origins for better security.
 */
@Configuration
public class CorsConfig {

    /**
     * Defines a WebMvcConfigurer bean that configures CORS mappings for the application.
     * @return a WebMvcConfigurer that allows all origins to access endpoints under "/api/**"
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:8080");
            }
        };
    }
}
