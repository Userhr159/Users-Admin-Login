package com.carlosrepo.SistemaEcomers.User.Dto;


import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchDTO {

    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Email(message = "Email inválido")
    private String email;
}
