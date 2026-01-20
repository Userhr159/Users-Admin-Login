package com.carlosrepo.SistemaEcomers.Login.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta de autenticación exitosa")
public class LoginResponseDTO {

    @Schema(
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJsb3NAY29ycmVvLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3Njg5NDM2OTYsImV4cCI6MTc2OTAzMDA5Nn0.xxxxx",
            description = "JWT que debe enviarse en Authorization: Bearer <token>"
    )
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
