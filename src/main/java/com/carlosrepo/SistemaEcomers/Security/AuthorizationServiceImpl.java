package com.carlosrepo.SistemaEcomers.Security;

import com.carlosrepo.SistemaEcomers.Login.Service.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public CustomUserDetails getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new AccessDeniedException("Usuario no autenticado");
        }

        return (CustomUserDetails) auth.getPrincipal();
    }

    @Override
    public Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }

    @Override
    public boolean isAdmin() {
        return getAuthenticatedUser().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public void checkSelf(Long userId) {
        if (!getAuthenticatedUserId().equals(userId)) {
            throw new AccessDeniedException("No puedes modificar este usuario");
        }
    }

    @Override
    public void checkSelfOrAdmin(Long userId) {
        if (!isAdmin() && !getAuthenticatedUserId().equals(userId)) {
            throw new AccessDeniedException("No autorizado");
        }
    }
}

