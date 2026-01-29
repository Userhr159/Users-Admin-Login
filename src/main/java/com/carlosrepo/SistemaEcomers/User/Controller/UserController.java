package com.carlosrepo.SistemaEcomers.User.Controller;

import com.carlosrepo.SistemaEcomers.Login.Dtos.ErrorResponse;
import com.carlosrepo.SistemaEcomers.User.Dto.*;
import com.carlosrepo.SistemaEcomers.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Registro de usuario",
            description = "Permite registrar un usuario con rol USER"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuario creado",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    // Registro público (rol USER por defecto)
    @PostMapping("/register")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@Valid @RequestBody UserCreateDTO dto) {
        return userService.create(dto);
    }

    @Operation(
            summary = "Crear usuario (ADMIN)",
            description = """
            Permite a un ADMIN crear usuarios con cualquier rol.
            Roles permitidos: USER, ADMIN
            """
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuario creado",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado"
    )
    // SOLO ADMIN puede crear usuarios con cualquier rol
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public UserResponseDTO createByAdmin(
            @Valid @RequestBody AdminCreateDTO dto) {

        return userService.createByAdmin(dto);
    }


    //
    @Operation(
            summary = "Actualizar usuario",
            description = """
                USER: solo puede modificar su propio usuario  
                ADMIN: puede modificar cualquier usuario  
                Si se cambian email o password, el token queda inválido
                """
    )
    @ApiResponse(responseCode = "200", description = "Usuario actualizado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patchUser(
            @PathVariable Long id,
            @Valid @RequestBody UserPatchDTO dto) {

        return ResponseEntity.ok(userService.patchUser(id, dto));
    }


    @Operation(
            summary = "Actualizar usuario (ADMIN)",
            description = """
            ADMIN puede modificar cualquier usuario.
            Puede cambiar nombre, email, password y rol.
            Si se cambian credenciales, el token queda inválido.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<UserResponseDTO> patchAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminPatchDTO dto) {

        return ResponseEntity.ok(userService.patchAdmin(id, dto));
    }



    @Operation(
            summary = "Obtener usuario autenticado",
            description = "Devuelve la información del usuario logueado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuario autenticado",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Token inválido o expirado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    //consulta usuario sin id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public UserResponseDTO me() {
        return userService.getMe();
    }


    @Operation(
            summary = "Eliminar usuario",
            description = "Permite a un ADMIN eliminar un usuario por ID"
    )
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    // SOLO ADMIN puede eliminar usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

