package com.example.carnation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Carnation API",
                description = "API documentation for Carnation Project",
                version = "1.0.0",
                contact = @Contact(
                        name = "Support Team",
                        email = "support@example.com",
                        url = "https://example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;  // ✅ 환경 변수 값 읽기

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(serverUrl))
                .components(new Components());
    }

    @Bean
    public OperationCustomizer addGlobalHeaders() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                    .in("header")
                    .name("ngrok-skip-browser-warning")
                    .description("Ngrok Skip")
                    .required(false)
                    .schema(new StringSchema()._default("69420"))
            );
            return operation;
        };
    }
}
