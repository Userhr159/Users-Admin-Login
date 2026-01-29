package com.carlosrepo.SistemaEcomers.User.Service;

import com.carlosrepo.SistemaEcomers.Login.Jwt.JwtFilter;
import com.carlosrepo.SistemaEcomers.User.Controller.UserController;
import com.carlosrepo.SistemaEcomers.User.Dto.AdminPatchDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserResponseDTO;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerAdminPatchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtFilter jwtFilter;


    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldPatchUserAsAdmin_whenValidRequest() throws Exception {

        AdminPatchDTO dto = new AdminPatchDTO();
        dto.setNombre("Nuevo Nombre");
        dto.setEmail("nuevo@correo.com");
        dto.setRole(RoleType.ROLE_USER);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setNombre("Nuevo Nombre");
        response.setEmail("nuevo@correo.com");
        response.setRole(RoleType.ROLE_USER);
        response.setCredentialsChanged(true);

        when(userService.patchAdmin(eq(1L), any(AdminPatchDTO.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/users/admin/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"))
                .andExpect(jsonPath("$.email").value("nuevo@correo.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.credentialsChanged").value(true));
    }

}
