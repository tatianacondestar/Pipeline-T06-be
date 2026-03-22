package ap1.AllaVamos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de Swagger OpenAPI 3 para la documentación del proyecto Experiencia Lunahuaná.
 *
 * URLs:
 *  ➤ http://localhost:8081/swagger-ui.html
 *  ➤ http://localhost:8081/v3/api-docs
 *
 * También habilita CORS global para el proyecto.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura un grupo de endpoints públicos de Swagger.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springboot-public")
                .pathsToMatch("/api/**")
                .build();
    }

    /**
     * Configuración general de OpenAPI (Swagger).
     */
    @Bean
    public OpenAPI apiInfo(@Value("${app.base-url:http://localhost:${server.port:8080}}") String serverUrl) {
        return new OpenAPI()
                .addServersItem(new Server().url(serverUrl))
                .info(new Info()
                        .title("REST API - Experiencia Lunahuaná")
                        .description("API REST desarrollada con Spring Boot y MongoDB.")
                        .version("1.0.0")
                        .license(new License()
                                .name("Valle Grande")
                                .url("https://vallegrande.edu.pe"))
                );
    }

    /**
     * Habilitar CORS global para WebFlux.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsWebFilter(source);
    }
}