package com.campusform.server.identity.domain.repository;

import java.util.Optional;

import com.campusform.server.identity.domain.model.User;

/**
 * User Repository 인터페이스 (도메인 관점)
 *
 * DDD 관점에서 Repository 인터페이스는 도메인 계층에 위치합니다.
 * (Spring Data JPA 같은 기술 의존은 infrastructure 구현체에서만 사용)
 */
public interface UserRepository {

    Optional<User> getUserById(Long adminId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsById(Long adminId);
}
