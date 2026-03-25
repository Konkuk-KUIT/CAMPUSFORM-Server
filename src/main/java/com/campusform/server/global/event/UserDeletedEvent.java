package com.campusform.server.global.event;

/**
 * 사용자 회원 탈퇴 이벤트
 *
 * Identity Context에서 사용자 계정이 삭제된 직후 발행됩니다.
 * 다른 바운디드 컨텍스트(프로젝트, 알림, 리크루팅 등)에서 연관 데이터 정리에 사용합니다.
 */
public record UserDeletedEvent(Long userId) {
}
