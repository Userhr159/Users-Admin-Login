package com.carlosrepo.SistemaEcomers.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sistema Ecomers API",
                version = "1.0",
                description = """
                        API REST para sistema de comercio electrónico.

                        Funcionalidades:
                        - Autenticación JWT
                        - Gestión de usuarios (USER / ADMIN)
                        - Control de permisos por rol
                        - Invalidación de sesión al cambiar credenciales
                        """
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
