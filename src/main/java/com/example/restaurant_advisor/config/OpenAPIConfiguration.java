package com.example.restaurant_advisor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

//   https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenAPIConfiguration {

}