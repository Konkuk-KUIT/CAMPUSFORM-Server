package com.campusform.server.notification.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusform.server.notification.domain.model.Notification;

/**
 * Spring Data JPA를 위한 Notification Repository
 *
 * 기본 CRUD 메서드와 커스텀 쿼리 메서드를 제공합니다.
 */
@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    /**
     * 사용자별 알림 목록 조회 (페이징 지원)
     * idx_receiver_created 인덱스 활용
     */
    Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    /**
     * 사용자의 안읽은 알림 개수
     */
    long countByReceiverIdAndReadAtIsNull(Long receiverId);
}
