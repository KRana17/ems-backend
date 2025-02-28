package com.example.ems.config;





import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    // Inject allowed origins from application properties
    @Value("${cors.allowed-origins:http://localhost}")
    private String allowedOriginsString;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CorsConfig.class);

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Split the comma-separated list of allowed origins
        List<String> allowedOrigins = Arrays.asList(allowedOriginsString.split(","));
        log.info("CORS configuration - Allowed origins: {}", allowedOrigins);
        allowedOrigins.forEach(origin -> {
            config.addAllowedOrigin(origin.trim());
        });

        // Allow common HTTP methods
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        // Allow all headers
        config.addAllowedHeader("*");

        // Allow credentials if needed (cookies, authentication)
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}