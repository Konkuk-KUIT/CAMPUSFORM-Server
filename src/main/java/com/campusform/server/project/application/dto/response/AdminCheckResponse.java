package com.campusform.server.project.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관리자 이메일 검증 응답 DTO
 * 
 * 프로젝트 생성 전에 이메일로 관리자를 추가할 수 있는지 검증한 결과를 담습니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCheckResponse {

    /**
     * 회원 존재 여부
     */
    private boolean exists;

    private Long userId;

    private String nickname;

    private String email;

    private String profileImageUrl;

    private String errorMessage;

    /**
     * 회원이 존재하는 경우의 생성자
     */
    public static AdminCheckResponse success(Long userId, String nickname, String email, String profileImageUrl) {
        return new AdminCheckResponse(true, userId, nickname, email, profileImageUrl, null);
    }

    /**
     * 회원이 존재하지 않는 경우의 생성자
     */
    public static AdminCheckResponse notFound(String email) {
        return new AdminCheckResponse(false, null, null, null, email,
                "해당 이메일로 가입된 회원이 없습니다: " + email);
    }
}
