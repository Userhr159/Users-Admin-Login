package com.carlosrepo.SistemaEcomers.Security;

import com.carlosrepo.SistemaEcomers.Login.Jwt.JwtUtil;
import com.carlosrepo.SistemaEcomers.Login.Service.CustomUserDetails;
import com.carlosrepo.SistemaEcomers.Login.Service.CustomUserDetailsService;
import com.carlosrepo.SistemaEcomers.User.Dto.AdminPatchDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserResponseDTO;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleEntity;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.carlosrepo.SistemaEcomers.User.Entity.UserEntity;
import com.carlosrepo.SistemaEcomers.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=mi-clave-super-secreta-para-jwt-512-bits",
        "jwt.expiration=86400000"
})
class UserAdminPatchSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private String generateToken(RoleType role) {

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleType(role);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("admin@correo.com");
        user.setPassword("123456");
        user.setRole(roleEntity);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        when(userDetailsService.loadUserByUsername(user.getEmail()))
                .thenReturn(userDetails);

        return jwtUtil.generateToken(userDetails);
    }

    // =========================
    // 401 - SIN TOKEN
    // =========================
    @Test
    void shouldReturn401_whenNoToken() throws Exception {

        mockMvc.perform(patch("/api/users/admin/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    // =========================
    // 403 - ROLE_USER
    // =========================
    @Test
    void shouldReturn403_whenUserRole() throws Exception {

        String token = generateToken(RoleType.ROLE_USER);

        AdminPatchDTO dto = new AdminPatchDTO();
        dto.setRole(RoleType.ROLE_USER); // DTO válido

        mockMvc.perform(patch("/api/users/admin/{id}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }


    // =========================
    // 200 - ROLE_ADMIN
    // =========================
    @Test
    void shouldReturn200_whenAdminRole() throws Exception {

        String token = generateToken(RoleType.ROLE_ADMIN);

        AdminPatchDTO dto = new AdminPatchDTO();
        dto.setNombre("Nuevo Nombre");
        dto.setRole(RoleType.ROLE_USER);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setNombre("Nuevo Nombre");
        response.setRole(RoleType.ROLE_USER);
        response.setCredentialsChanged(false);

        when(userService.patchAdmin(eq(1L), any(AdminPatchDTO.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/users/admin/{id}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
