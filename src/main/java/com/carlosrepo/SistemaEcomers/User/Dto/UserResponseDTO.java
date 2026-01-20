package com.carlosrepo.SistemaEcomers.User.Dto;

import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta del usuario")
public class UserResponseDTO {
    private Long id;
    @Schema(example = "Carlos")
    private String nombre;
    @Schema(example = "carlos@correo.com")
    private String email;
    @Schema(example = "ROLE_USER")
    private String role;
    @Schema(
            example = "false",
            description = "Indica si se modificaron credenciales y el token debe renovarse"
    )
    private boolean credentialsChanged;
}
