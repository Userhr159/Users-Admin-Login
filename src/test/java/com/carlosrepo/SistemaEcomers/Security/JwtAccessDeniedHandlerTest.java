package com.carlosrepo.SistemaEcomers.Security;

import com.carlosrepo.SistemaEcomers.Login.Dtos.ErrorResponse;
import com.carlosrepo.SistemaEcomers.Login.Jwt.JwtAccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAccessDeniedHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private final JwtAccessDeniedHandler handler =
            new JwtAccessDeniedHandler(objectMapper);

    @Test
    void shouldReturn403WithJsonBody_whenAccessDenied() throws Exception {

        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin");

        MockHttpServletResponse response = new MockHttpServletResponse();

        AccessDeniedException exception =
                new AccessDeniedException("Forbidden");

        // Act
        handler.handle(request, response, exception);

        // Assert HTTP
        assertEquals(403, response.getStatus());
        assertEquals("application/json", response.getContentType());

        // Assert Body
        ErrorResponse body = objectMapper.readValue(
                response.getContentAsByteArray(),
                ErrorResponse.class
        );

        assertEquals(403, body.getStatus());
        assertEquals("No tienes permisos para esta acción", body.getMessage());
        assertEquals("/api/admin", body.getPath());
        assertNotNull(body.getTimestamp());
    }
}
