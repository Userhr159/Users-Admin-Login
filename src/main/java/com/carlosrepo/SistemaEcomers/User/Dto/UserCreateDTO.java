package com.carlosrepo.SistemaEcomers.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
//dto para registro publico
@Getter
@Setter
@Schema(name = "UserCreateDTO", description = "Datos para registro de usuario")

public class UserCreateDTO {

    @NotBlank
    @Email
    @Schema(
            example = "carlos@correo.com",
            description = "Email único del usuario"
    )
    private String email;

    @Schema(
            example = "123456",
            description = "Contraseña en texto plano"
    )
    @NotBlank
    @Size(max = 6)
    private String password;

    @Schema(
            example = "Carlos",
            description = "Nombre del usuario"
    )
    @NotBlank
    @Size(min = 2, max = 30)
    private String nombre;
}
