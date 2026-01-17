package com.campusform.server.identity.application.dto.response;

/**
 * 로그아웃 응답 DTO (POST /api/auth/logout)
 */
public record LogoutResponse(
        String status,
        String message
) {
    /**
     * 로그아웃 성공 응답 생성
     */
    public static LogoutResponse success() {
        return new LogoutResponse("success", "로그아웃하였습니다.");
    }
}
