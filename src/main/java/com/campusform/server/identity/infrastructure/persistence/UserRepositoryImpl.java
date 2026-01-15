package com.campusform.server.identity.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.campusform.server.identity.domain.model.User;
import com.campusform.server.identity.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * UserRepository 구현체 (infrastructure)
 *
 * 도메인 Repository 인터페이스(UserRepository)를 구현하고,
 * 내부적으로 Spring Data JPA Repository를 사용합니다.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> getUserById(Long adminId) {
        return jpaUserRepository.findById(adminId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public boolean existsById(Long adminId) {
        return jpaUserRepository.existsById(adminId);
    }
}
