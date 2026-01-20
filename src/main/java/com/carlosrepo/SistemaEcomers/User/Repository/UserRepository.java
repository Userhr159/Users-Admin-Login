package com.carlosrepo.SistemaEcomers.User.Repository;

import com.carlosrepo.SistemaEcomers.User.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);

}
