package com.pravin.learnsphere_backend_with_spring_boot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Courses Management API",
        version = "1.0",
        description = "API documentation for Courses and Course Instances management."
    )
)
public class OpenApiConfig {
    // No additional configuration needed for basic setup
}
