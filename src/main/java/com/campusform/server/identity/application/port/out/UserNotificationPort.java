package com.campusform.server.identity.application.port.out;

/**
 * 알림 컨텍스트의 사용자 알림 설정 기능을 참조하기 위한 Port (Output Port)
 *
 * Identity Context가 Notification Context의 사용자 알림 설정을 변경해야 할 때 사용됩니다.
 * 이를 통해 Identity Context는 Notification Context의 구현 세부 사항에 직접 의존하지 않습니다.
 */
public interface UserNotificationPort {

    /**
     * 특정 사용자의 알림 수신 설정을 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 현재 알림 수신 설정 여부 (true: 활성화, false: 비활성화)
     */
    boolean getNotificationSetting(Long userId);

    /**
     * 특정 사용자의 알림 수신 설정을 업데이트하거나 생성합니다.
     *
     * @param userId             설정을 변경할 사용자 ID
     * @param isNotificationEnabled 변경할 알림 수신 설정 (true: 활성화, false: 비활성화)
     * @return 변경된 알림 수신 설정 여부 (true: 활성화, false: 비활성화)
     */
    boolean updateNotificationSettings(Long userId, boolean isNotificationEnabled);
}
