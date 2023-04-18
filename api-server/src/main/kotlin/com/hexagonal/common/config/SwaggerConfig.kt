package com.hexagonal.common.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 스웨거 설정
 */
@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Klone Studio API",
        description = "Klone Studio API",
        version = "1.0",
    ),
)
class SwaggerConfig {
    /**
     * Klone sales 전체 api
     */
    @Bean
    fun kloneSalesApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("Klone Studio api")
            .pathsToMatch("/api/v1/**")
            .addOpenApiCustomiser(buildSecurityOpenApi())
            .build()
    }

    /**
     * 인증관련
     */
    fun buildSecurityOpenApi(): OpenApiCustomiser? {
        // jwt token 설정 필요시 유지
        return OpenApiCustomiser { OpenApi: OpenAPI ->
            OpenApi.addSecurityItem(SecurityRequirement().addList("jwt token"))
                .components.addSecuritySchemes(
                    "jwt token",
                    SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .`in`(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer"),
                )
        }
    }
}
