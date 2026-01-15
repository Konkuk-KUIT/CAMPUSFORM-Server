package com.campusform.server.identity.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.campusform.server.identity.domain.model.User;
import com.campusform.server.identity.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * UserRepository 구현체
 * 
 * 적절히 SpringDataJpa 또는 Querydsl에 작업을 위임합니다.
 * 
 * 따라서 Repository를 사용할 때 본 인터페이스를 사용합니다.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

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

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }
}
