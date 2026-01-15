package com.campusform.server.identity.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserExistsResponse {
    private boolean exists;
    private Long userId;
    private String nickname;
    private String email;
    private String profileImageUrl;

    public static UserExistsResponse found(Long userId, String nickname, String email, String profileImageUrl) {
        return new UserExistsResponse(true, userId, nickname, email, profileImageUrl);
    }

    public static UserExistsResponse notFound(String email) {
        return new UserExistsResponse(false, null, null, email, null);
    }
}
