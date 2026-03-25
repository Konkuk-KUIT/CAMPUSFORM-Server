package com.campusform.server.notification.application.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.global.event.UserDeletedEvent;
import com.campusform.server.notification.domain.repository.UserNotificationSettingsRepository;
import com.campusform.server.notification.infrastructure.persistence.NotificationJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationUserEventListener {

    private final UserNotificationSettingsRepository settingsRepository;
    private final NotificationJpaRepository notificationJpaRepository;

    @EventListener
    @Transactional
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        Long userId = event.userId();
        log.info("UserDeletedEvent 수신: userId={}", userId);

        settingsRepository.findByUserId(userId).ifPresent(settings -> {
            log.info("사용자 알림 설정 삭제: userId={}", userId);
            settingsRepository.delete(settings);
        });

        log.info("사용자 알림 삭제: receiverId={}", userId);
        notificationJpaRepository.deleteByReceiverId(userId);
    }
}
