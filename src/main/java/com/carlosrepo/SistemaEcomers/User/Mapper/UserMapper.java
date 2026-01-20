package com.carlosrepo.SistemaEcomers.User.Mapper;

import com.carlosrepo.SistemaEcomers.User.Dto.UserCreateDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserResponseDTO;
import com.carlosrepo.SistemaEcomers.User.Entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserCreateDTO dto) {
        UserEntity user = new UserEntity();
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDTO toDTO(UserEntity entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());

        if (entity.getRole() != null && entity.getRole().getNombre() != null) {
            dto.setRole(entity.getRole().getNombre().name());
        }

        return dto;
    }


    public static UserResponseDTO toDTO(UserEntity entity, boolean credentialsChanged) {
        UserResponseDTO dto = toDTO(entity);
        dto.setCredentialsChanged(credentialsChanged);
        return dto;
    }
}


