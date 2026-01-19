package com.campusform.server.notification.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 안읽은 알림 개수 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnreadCountResponse {

    private long unreadCount;

    public static UnreadCountResponse of(long count) {
        return new UnreadCountResponse(count);
    }

    /**
     * 알림 설정 OFF 시 사용
     */
    public static UnreadCountResponse zero() {
        return new UnreadCountResponse(0);
    }
}
