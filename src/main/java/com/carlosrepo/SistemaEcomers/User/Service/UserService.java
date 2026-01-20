package com.carlosrepo.SistemaEcomers.User.Service;

import com.carlosrepo.SistemaEcomers.User.Dto.*;

public interface UserService {

    UserResponseDTO create(UserCreateDTO dto);

    UserResponseDTO createByAdmin(AdminCreateDTO dto);


    UserResponseDTO patchUser(Long id, UserPatchDTO dto);

    UserResponseDTO patchAdmin(Long id, AdminPatchDTO dto);

    UserResponseDTO getMe();

    void delete(Long id);
}
