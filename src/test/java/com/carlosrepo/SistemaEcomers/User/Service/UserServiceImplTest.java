package com.carlosrepo.SistemaEcomers.User.Service;

import com.carlosrepo.SistemaEcomers.Common.exception.BusinessException;
import com.carlosrepo.SistemaEcomers.Security.AuthorizationService;
import com.carlosrepo.SistemaEcomers.User.Dto.UserCreateDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserPatchDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserResponseDTO;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleEntity;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.carlosrepo.SistemaEcomers.User.Entity.UserEntity;
import com.carlosrepo.SistemaEcomers.User.Repository.RoleRepository;
import com.carlosrepo.SistemaEcomers.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_shouldCreateUserWithRoleUser() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setNombre("Carlos");
        dto.setEmail("carlos@test.com");
        dto.setPassword("123456");

        RoleEntity roleUser = new RoleEntity(1L, RoleType.ROLE_USER);

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleType(RoleType.ROLE_USER))
                .thenReturn(Optional.of(roleUser));
        when(passwordEncoder.encode(dto.getPassword()))
                .thenReturn("encodedPass");

        UserResponseDTO response = userService.create(dto);

        assertNotNull(response);
        assertEquals("Carlos", response.getNombre());
        assertEquals("carlos@test.com", response.getEmail());
        assertEquals(RoleType.ROLE_USER, response.getRole());

        verify(userRepository).save(any(UserEntity.class));
    }
    @Test
    void create_shouldThrowException_whenEmailExists() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("carlos@test.com");

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> userService.create(dto)
        );

        assertEquals("El email ya está en uso", ex.getMessage());

        verify(userRepository, never()).save(any());
    }
    @Test
    void patchUser_shouldUpdateUser_whenSelf() {

        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setNombre("Old");
        user.setEmail("old@test.com");
        user.setPassword("oldPass");

        UserPatchDTO dto = new UserPatchDTO();
        dto.setNombre("New Name");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = userService.patchUser(userId, dto);

        assertEquals("New Name", response.getNombre());

        verify(authorizationService).checkSelf(userId);
        verify(userRepository).save(user);
    }

    @Test
    void patchUser_shouldThrowAccessDenied_whenNotSelf() {

        Long userId = 99L;

        doThrow(new AccessDeniedException("No autorizado"))
                .when(authorizationService).checkSelf(userId);

        UserPatchDTO dto = new UserPatchDTO();

        assertThrows(
                AccessDeniedException.class,
                () -> userService.patchUser(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }
    @Test
    void getMe_shouldReturnAuthenticatedUser() {

        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setNombre("Carlos");
        user.setEmail("carlos@test.com");

        when(authorizationService.getAuthenticatedUserId())
                .thenReturn(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getMe();

        assertEquals("Carlos", response.getNombre());
    }




}
