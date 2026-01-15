package com.campusform.server.project.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.identity.domain.repository.UserRepository;
import com.campusform.server.project.application.dto.request.CreateProjectRequest;
import com.campusform.server.project.application.dto.response.AdminCheckResponse;
import com.campusform.server.project.application.dto.response.ProjectResponse;
import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.project.domain.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final SpreadsheetService spreadsheetService;

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * 프로젝트 생성
     * 
     * @throws IllegalStateException 스프레드시트 연동이 완료되지 않은 경우
     */

    @Transactional
    public ProjectResponse createProject(Long ownerId, CreateProjectRequest request) {
        List<Long> adminIds = request.getAdminIds() == null ? List.of() : request.getAdminIds();
        adminIds.stream().distinct().forEach(adminId -> {
            if (!userRepository.existsById(adminId))
                throw new IllegalArgumentException("존재하지 않는 회원입니다. adminId=" + adminId);
        });

        /**
         * Project 생성 -> 연관관계 설정
         */
        Project project = Project.create(
                request.getTitle(),
                ownerId,
                request.getSheetUrl(),
                request.getStartAt(),
                request.getEndAt());
        adminIds.stream().distinct().forEach(project::addAdmin);
        project.addMapping(request.getRequiredMappings());

        Project savedProject = projectRepository.save(project);

        // 스프레드 시트 연동
        spreadsheetService.syncInit(request.getSheetUrl());

        // 응답 DTO로 변환하여 반환
        return ProjectResponse.from(savedProject);
    }

    /**
     * 이메일 -> 회원 존재 여부
     */
    @Transactional(readOnly = true)
    public AdminCheckResponse checkAdminByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> AdminCheckResponse.success(
                        user.getId(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getProfileImageUrl()))
                .orElse(AdminCheckResponse.notFound(email));
    }
}
