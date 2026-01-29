package com.carlosrepo.SistemaEcomers.User.Dto;

import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPatchDTO extends UserPatchDTO {
    @NotNull
    private RoleType role;
}
