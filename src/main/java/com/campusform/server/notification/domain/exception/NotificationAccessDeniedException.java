package com.campusform.server.notification.domain.exception;

/**
 * 알림 접근 권한이 없을 때 발생하는 예외
 * HTTP 403 Forbidden으로 매핑됩니다.
 */
public class NotificationAccessDeniedException extends RuntimeException {

    public NotificationAccessDeniedException(String message) {
        super(message);
    }

    public NotificationAccessDeniedException(Long notificationId, Long userId) {
        super("해당 알림에 대한 접근 권한이 없습니다. notificationId=" + notificationId + ", userId=" + userId);
    }
}
