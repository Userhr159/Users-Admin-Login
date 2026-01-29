package com.carlosrepo.SistemaEcomers.Login.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Error estándar de la API")
public class ErrorResponse {

    @Schema(example = "401")
    private int status;

    @Schema(example = "No autenticado o token inválido")
    private String message;

    @Schema(example = "/api/users/me")
    private String path;

    @Schema(example = "2026-01-20T16:30:00")
    private LocalDateTime timestamp;

    // 🔹 Constructor vacío (OBLIGATORIO para Jackson)
    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
