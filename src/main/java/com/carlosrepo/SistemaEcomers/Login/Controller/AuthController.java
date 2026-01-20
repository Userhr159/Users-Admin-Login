package com.carlosrepo.SistemaEcomers.Login.Controller;

import com.carlosrepo.SistemaEcomers.Common.exception.BusinessException;
import com.carlosrepo.SistemaEcomers.Login.Dtos.ErrorResponse;
import com.carlosrepo.SistemaEcomers.Login.Dtos.LoginRequestDTO;
import com.carlosrepo.SistemaEcomers.Login.Dtos.LoginResponseDTO;
import com.carlosrepo.SistemaEcomers.Login.Jwt.JwtUtil;
import com.carlosrepo.SistemaEcomers.Login.Service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Login de usuario",
            description = """
                    Autentica un usuario mediante email y contraseña.
                    
                    Retorna un JWT que debe enviarse en el header:
                    Authorization: Bearer <token>
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {

        try {

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );

            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(user);

            return new LoginResponseDTO(token);

        } catch (BadCredentialsException e) {
            throw new BusinessException("Email o contraseña incorrectos");
        }
    }



}
