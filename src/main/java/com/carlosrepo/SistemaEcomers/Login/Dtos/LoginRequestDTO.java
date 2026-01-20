package com.carlosrepo.SistemaEcomers.Login.Dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Credenciales para autenticación")
public class LoginRequestDTO {

    @Email(message = "Email inválido")
    @Schema(
            example = "carlos@correo.com",
            description = "Email del usuario registrado"
    )
    private String email;

    @Size(min = 6)
    @Schema(
            example = "123456",
            description = "Contraseña del usuario"
    )
    private String password;
}
