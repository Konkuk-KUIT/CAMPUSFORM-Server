package com.campusform.server.identity.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusform.server.identity.domain.model.User;

/**
 * Spring Data JPA를 위한 User Repository
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long adminId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsById(Long adminId);
}
