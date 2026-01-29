package com.carlosrepo.SistemaEcomers.User.Service;

import com.carlosrepo.SistemaEcomers.Common.exception.BusinessException;
import com.carlosrepo.SistemaEcomers.Security.AuthorizationService;
import com.carlosrepo.SistemaEcomers.User.Dto.*;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleEntity;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.carlosrepo.SistemaEcomers.User.Entity.UserEntity;
import com.carlosrepo.SistemaEcomers.User.Mapper.UserMapper;
import com.carlosrepo.SistemaEcomers.User.Repository.RoleRepository;
import com.carlosrepo.SistemaEcomers.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    // ========================
    // Helpers de dominio
    // ========================

    private UserEntity getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("El email ya está en uso");
        }
    }

    // ========================
    // Creación
    // ========================

    @Override
    public UserResponseDTO create(UserCreateDTO dto) {

        validateEmail(dto.getEmail());

        UserEntity user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        RoleEntity roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new BusinessException("Rol USER no existe"));

        user.setRole(roleUser);

        userRepository.save(user);
        return UserMapper.toDTO(user, false);
    }

    @Override
    public UserResponseDTO createByAdmin(AdminCreateDTO dto) {

        validateEmail(dto.getEmail());

        RoleEntity roleEntity = roleRepository.findByRoleType(dto.getRole())
                .orElseThrow(() -> new BusinessException("Rol no válido"));

        UserEntity user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(roleEntity);

        userRepository.save(user);
        return UserMapper.toDTO(user, false);
    }

    // ========================
    // Actualización USER
    // ========================

    @Override
    public UserResponseDTO patchUser(Long id, UserPatchDTO dto) {

        authorizationService.checkSelf(id);

        UserEntity user = getUser(id);
        boolean credentialsChanged = false;

        if (dto.getNombre() != null) {
            user.setNombre(dto.getNombre());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            validateEmail(dto.getEmail());
            user.setEmail(dto.getEmail());
            credentialsChanged = true;
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            credentialsChanged = true;
        }

        userRepository.save(user);
        return UserMapper.toDTO(user, credentialsChanged);
    }

    // ========================
    // Actualización ADMIN
    // ========================

    @Override
    public UserResponseDTO patchAdmin(Long id, AdminPatchDTO dto) {

        UserEntity user = getUser(id);
        boolean credentialsChanged = false;

        if (dto.getNombre() != null) {
            user.setNombre(dto.getNombre());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            validateEmail(dto.getEmail());
            user.setEmail(dto.getEmail());
            credentialsChanged = true;
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            credentialsChanged = true;
        }

        if (dto.getRole() != null) {
            RoleEntity role = roleRepository.findByRoleType(dto.getRole())
                    .orElseThrow(() -> new BusinessException("Rol inválido"));
            user.setRole(role);
        }

        userRepository.save(user);
        return UserMapper.toDTO(user, credentialsChanged);
    }

    // ========================
    // Eliminación
    // ========================

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // ========================
    // Usuario autenticado
    // ========================

    @Override
    public UserResponseDTO getMe() {

        Long userId = authorizationService.getAuthenticatedUserId();

        UserEntity user = getUser(userId);
        return UserMapper.toDTO(user, false);
    }
}
