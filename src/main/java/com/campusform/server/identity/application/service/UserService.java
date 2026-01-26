package com.campusform.server.identity.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.campusform.server.global.infrastructure.S3Service;
import com.campusform.server.identity.domain.model.User;
import com.campusform.server.identity.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 정보 수정 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    /**
     * 프로필 이미지 업데이트
     *
     * @param userId 사용자 ID
     * @param imageFile 새로운 프로필 이미지 파일
     * @return 업데이트된 프로필 이미지 URL
     */
    @Transactional
    public String updateProfileImage(Long userId, MultipartFile imageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 기존 프로필 이미지가 S3에 있다면 삭제
        String oldProfileImageUrl = user.getProfileImageUrl();
        if (oldProfileImageUrl != null)
            s3Service.deleteFile(oldProfileImageUrl);

        // 새 이미지 업로드
        String newProfileImageUrl = s3Service.uploadProfileImage(imageFile, userId);

        // 사용자 정보 업데이트
        user.updateProfileImage(newProfileImageUrl);

        log.info("프로필 이미지 업데이트 완료: userId={}, newUrl={}", userId, newProfileImageUrl);

        return newProfileImageUrl;
    }

    /**
     * 프로필 이미지 삭제
     *
     * @param userId 사용자 ID
     */
    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String profileImageUrl = user.getProfileImageUrl();

        // S3에서 이미지 삭제
        if (profileImageUrl != null)
            s3Service.deleteFile(profileImageUrl);

        // 사용자 프로필 이미지 null로 설정
        user.updateProfileImage(null);

        log.info("프로필 이미지 삭제 완료: userId={}", userId);
    }

    /**
     * 닉네임 수정
     *
     * @param userId 사용자 ID
     * @param newNickname 새로운 닉네임
     * @return 수정된 닉네임
     */
    @Transactional
    public String updateNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.updateNickname(newNickname);

        log.info("닉네임 수정 완료: userId={}, newNickname={}", userId, newNickname);

        return user.getNickname();
    }
}
