package com.banking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Mini Banking System API",
                version = "1.0",
                description = "REST API for customer, account, transaction and fund transfer management."
        )
)
public class OpenApiConfig {
}