package com.campusform.server.identity.presentation;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campusform.server.identity.domain.model.User;
import com.campusform.server.identity.domain.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * "프로젝트 생성 전 관리자 추가"를 위한 회원 존재 검증 API
 *
 * - 프론트에서 이메일을 입력받아 '추가' 버튼을 누르기 전에,
 *   서버에 존재 여부를 확인해서 미가입이면 바로 안내할 수 있습니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    /**
     * 이메일로 회원 존재 여부 확인
     *
     * - 존재하면 userId/nickname을 함께 내려주면,
     *   프론트에서 '추가할 관리자' 목록을 구성하기 쉽습니다.
     */
    @GetMapping("/exists")
    public ResponseEntity<UserExistsResponse> existsByEmail(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(new UserExistsResponse(false, null, null, email));
        }

        User user = userOpt.get();
        return ResponseEntity.ok(new UserExistsResponse(true, user.getId(), user.getNickname(), user.getEmail()));
    }

    @Getter
    @AllArgsConstructor
    public static class UserExistsResponse {
        private boolean exists;
        private Long userId;
        private String nickname;
        private String email;
    }
}

