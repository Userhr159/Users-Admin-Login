package com.carlosrepo.SistemaEcomers.User.Dto;

import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(name = "AdminCreateDTO")
public class AdminCreateDTO extends UserCreateDTO {
    @NotNull
    private RoleType role;
}

