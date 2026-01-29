package com.carlosrepo.SistemaEcomers.Config;

import com.carlosrepo.SistemaEcomers.User.Entity.RoleEntity;
import com.carlosrepo.SistemaEcomers.User.Entity.RoleType;
import com.carlosrepo.SistemaEcomers.User.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.findByRoleType(RoleType.ROLE_USER).isEmpty()) {
            roleRepository.save(new RoleEntity(null, RoleType.ROLE_USER));
        }

        if (roleRepository.findByRoleType(RoleType.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new RoleEntity(null, RoleType.ROLE_ADMIN));
        }
    }

}
