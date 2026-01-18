package com.campusform.server.notification.infrastructure.adapter;

import org.springframework.stereotype.Component;

import com.campusform.server.identity.application.port.out.UserNotificationPort;
import com.campusform.server.notification.application.service.NotificationService;

import lombok.RequiredArgsConstructor;

/**
 * Identity Context의 UserNotificationPort에 대한 Adapter 구현체
 *
 * 이 어댑터는 Notification Context 내에 존재하며, Identity Context의 요청을
 * NotificationService의 실제 비즈니스 로직으로 연결하는 역할을 합니다.
 */
@Component
@RequiredArgsConstructor
public class UserNotificationAdapter implements UserNotificationPort {

    private final NotificationService notificationService;

    @Override
    public boolean getNotificationSetting(Long userId) {
        return notificationService.getNotificationSetting(userId);
    }

    @Override
    public boolean updateNotificationSettings(Long userId, boolean isNotificationEnabled) {
        return notificationService.updateNotificationSetting(userId, isNotificationEnabled);
    }
}
