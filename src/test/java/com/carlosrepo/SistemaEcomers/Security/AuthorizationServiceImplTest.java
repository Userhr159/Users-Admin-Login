package com.carlosrepo.SistemaEcomers.Security;

import com.carlosrepo.SistemaEcomers.Login.Service.CustomUserDetails;
import com.carlosrepo.SistemaEcomers.Security.AuthorizationServiceImpl;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleEntity;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.carlosrepo.SistemaEcomers.User.Entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationServiceImplTest {

    private final AuthorizationServiceImpl authorizationService =
            new AuthorizationServiceImpl();

    @AfterEach
    void cleanContext() {
        SecurityContextHolder.clearContext();
    }

    // ---------- helpers ----------

    private void authenticateAs(Long userId, RoleType roleType) {

        RoleEntity role = new RoleEntity();
        role.setRoleType(roleType);

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("test@mail.com");
        user.setPassword("encoded");
        user.setRole(role);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // ---------- tests ----------

    @Test
    void getAuthenticatedUserId_shouldReturnUserId() {
        authenticateAs(1L, RoleType.ROLE_USER);

        Long userId = authorizationService.getAuthenticatedUserId();

        assertEquals(1L, userId);
    }

    @Test
    void checkSelf_shouldPassWhenSameUser() {
        authenticateAs(1L, RoleType.ROLE_USER);

        assertDoesNotThrow(() ->
                authorizationService.checkSelf(1L)
        );
    }

    @Test
    void checkSelf_shouldThrowWhenDifferentUser() {
        authenticateAs(1L, RoleType.ROLE_USER);

        assertThrows(AccessDeniedException.class, () ->
                authorizationService.checkSelf(2L)
        );
    }

    @Test
    void checkSelfOrAdmin_shouldPassForAdmin() {
        authenticateAs(99L, RoleType.ROLE_ADMIN);

        assertDoesNotThrow(() ->
                authorizationService.checkSelfOrAdmin(1L)
        );
    }

    @Test
    void checkSelfOrAdmin_shouldPassForSelfUser() {
        authenticateAs(1L, RoleType.ROLE_USER);

        assertDoesNotThrow(() ->
                authorizationService.checkSelfOrAdmin(1L)
        );
    }

    @Test
    void checkSelfOrAdmin_shouldThrowWhenNotAdminNorSelf() {
        authenticateAs(1L, RoleType.ROLE_USER);

        assertThrows(AccessDeniedException.class, () ->
                authorizationService.checkSelfOrAdmin(2L)
        );
    }
}
