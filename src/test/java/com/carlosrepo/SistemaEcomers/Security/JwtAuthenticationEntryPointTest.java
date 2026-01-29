package com.carlosrepo.SistemaEcomers.Security;

import com.carlosrepo.SistemaEcomers.Login.Dtos.ErrorResponse;
import com.carlosrepo.SistemaEcomers.Login.Jwt.JwtAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationEntryPointTest {

    private final JwtAuthenticationEntryPoint entryPoint =
            new JwtAuthenticationEntryPoint();

    // 🔹 ObjectMapper configurado correctamente
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldReturn401WithJsonBody_whenUnauthorized() throws Exception {

        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException exception =
                new BadCredentialsException("Token inválido");

        // Act
        entryPoint.commence(request, response, exception);

        // Assert HTTP
        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());

        // Assert Body
        ErrorResponse body = objectMapper.readValue(
                response.getContentAsByteArray(),
                ErrorResponse.class
        );

        assertEquals(401, body.getStatus());
        assertEquals("No autenticado o token inválido", body.getMessage());
        assertEquals("/api/test", body.getPath());
        assertNotNull(body.getTimestamp());
    }
}
