package com.carlosrepo.SistemaEcomers.Security;

import com.carlosrepo.SistemaEcomers.Login.Service.CustomUserDetails;

public interface AuthorizationService {

    CustomUserDetails getAuthenticatedUser();

    Long getAuthenticatedUserId();

    void checkSelf(Long userId);

    void checkSelfOrAdmin(Long userId);

    boolean isAdmin();
}
