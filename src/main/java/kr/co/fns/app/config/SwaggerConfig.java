package kr.co.fns.app.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.*;

@OpenAPIDefinition(
        info = @Info(title = "Fantoo2.0 App API 명세서",
                description = "기존 fantoo api 1.0 마이그레이션 자바 버전",
                version = "v1"))
@Component
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        var authHeader = Map.of(
            "bearAuth",
            new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .scheme("access_token")
                    .bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER).name("access_token")
        );
        var schemaRequirement = new SecurityRequirement().addList("bearAuth");

        return new OpenAPI()
                .components(new Components().securitySchemes(authHeader))
                .security(List.of(schemaRequirement));

    }
}

