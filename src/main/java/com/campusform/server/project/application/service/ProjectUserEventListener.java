package com.campusform.server.project.application.service;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.global.event.UserDeletedEvent;
import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.project.domain.repository.GoogleOAuthTokenRepository;
import com.campusform.server.project.domain.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectUserEventListener {

    private final ProjectRepository projectRepository;
    private final GoogleOAuthTokenRepository tokenRepository;

    @EventListener
    @Transactional
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        Long userId = event.userId();
        log.info("UserDeletedEvent 수신: userId={}", userId);

        // 사용자가 속한 프로젝트 목록 조회 (Owner 또는 Admin)
        List<Project> projects = projectRepository.findByUserId(userId);

        for (Project project : projects) {
            if (project.getOwnerId().equals(userId)) {
                // 사용자가 Owner인 경우 프로젝트 삭제
                log.info("프로젝트 삭제 (Owner 탈퇴): projectId={}, ownerId={}", project.getId(), userId);
                projectRepository.delete(project);
            } else {
                // 사용자가 Admin인 경우 관리자 목록에서 제거
                log.info("프로젝트 관리자 제거 (Admin 탈퇴): projectId={}, adminId={}", project.getId(), userId);
                project.removeAdmin(userId);
            }
        }

        // Google OAuth 토큰 삭제
        tokenRepository.findByOwnerId(userId).ifPresent(token -> {
            log.info("Google OAuth 토큰 삭제: ownerId={}", userId);
            tokenRepository.delete(token);
        });
    }
}
