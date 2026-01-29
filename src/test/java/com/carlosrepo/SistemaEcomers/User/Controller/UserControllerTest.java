package com.carlosrepo.SistemaEcomers.User.Controller;

import com.carlosrepo.SistemaEcomers.Login.Jwt.JwtFilter;
import com.carlosrepo.SistemaEcomers.User.Dto.UserCreateDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserPatchDTO;
import com.carlosrepo.SistemaEcomers.User.Dto.UserResponseDTO;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.carlosrepo.SistemaEcomers.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtFilter jwtFilter;


    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldRegisterUser_whenValidRequest() throws Exception {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("carlos@correo.com");
        dto.setPassword("123456");
        dto.setNombre("Carlos");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setEmail("carlos@correo.com");
        response.setNombre("Carlos");
        response.setRole(RoleType.ROLE_USER);
        response.setCredentialsChanged(false);

        when(userService.create(any(UserCreateDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("carlos@correo.com"))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }
    @Test
    void shouldReturn400_whenInvalidRequest() throws Exception {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("correo-invalido");
        dto.setPassword("");
        dto.setNombre("");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturnAuthenticatedUser_whenGetMe() throws Exception {

        // Arrange
        UserResponseDTO response = new UserResponseDTO();
        response.setId(10L);
        response.setNombre("Carlos");
        response.setEmail("carlos@correo.com");
        response.setRole(RoleType.ROLE_USER);
        response.setCredentialsChanged(false);

        when(userService.getMe()).thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@correo.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.credentialsChanged").value(false));
    }

    @Test
    void shouldPatchUser_whenValidRequest() throws Exception {

        // Arrange
        UserPatchDTO dto = new UserPatchDTO();
        dto.setNombre("Carlos Nuevo");
        dto.setEmail("nuevo@correo.com");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setNombre("Carlos Nuevo");
        response.setEmail("nuevo@correo.com");
        response.setRole(RoleType.ROLE_USER);
        response.setCredentialsChanged(true);

        when(userService.patchUser(eq(1L), any(UserPatchDTO.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Carlos Nuevo"))
                .andExpect(jsonPath("$.email").value("nuevo@correo.com"))
                .andExpect(jsonPath("$.credentialsChanged").value(true));
    }

    @Test
    void shouldReturn400_whenPatchUserInvalidRequest() throws Exception {

        UserPatchDTO dto = new UserPatchDTO();
        dto.setNombre("A");
        dto.setEmail("correo-invalido");

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldPatchUser_whenEmptyBody() throws Exception {

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setNombre("Carlos");
        response.setEmail("carlos@correo.com");
        response.setRole(RoleType.ROLE_USER);
        response.setCredentialsChanged(false);

        when(userService.patchUser(eq(1L), any(UserPatchDTO.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.credentialsChanged").value(false));
    }
}
