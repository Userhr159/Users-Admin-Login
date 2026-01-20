package com.carlosrepo.SistemaEcomers.User.Dto;

import lombok.Getter;
import lombok.Setter;
//Dto paara registro de admin
@Getter
@Setter
public class UserUpdateDTO {
    private String nombre;
    private String role; // ROLE_USER o ROLE_ADMIN
    private String password;
}
