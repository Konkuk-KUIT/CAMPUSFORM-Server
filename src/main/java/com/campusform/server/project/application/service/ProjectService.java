package com.campusform.server.project.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.identity.domain.repository.UserRepository;
import com.campusform.server.project.application.dto.request.CreateProjectRequest;
import com.campusform.server.project.application.dto.response.AdminCheckResponse;
import com.campusform.server.project.application.dto.response.ProjectResponse;
import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.project.domain.model.setting.ProjectAdmin;
import com.campusform.server.project.domain.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

/**
 * 프로젝트 관련 비즈니스 로직을 처리하는 서비스
 * 
 * 서비스 계층의 역할:
 * 1. 트랜잭션 관리 (@Transactional)
 * 2. 도메인 로직 조합 및 오케스트레이션
 * 3. 예외 처리 및 검증
 * 4. Repository를 통한 데이터 영속화
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * 프로젝트 생성
     * 
     * 트랜잭션으로 묶어서 프로젝트와 관리자, 컬럼 매핑 정보를
     * 모두 함께 저장하거나 모두 롤백되도록 보장합니다.
     * 
     * 비즈니스 규칙: 스프레드시트 연동이 완료되어야만 프로젝트를 생성할 수 있습니다.
     * 
     * @param request 프로젝트 생성 요청
     * @param ownerId 프로젝트 소유자 ID (현재 로그인한 사용자)
     * @return 생성된 프로젝트 정보
     * @throws IllegalStateException 스프레드시트 연동이 완료되지 않은 경우
     */

    @Transactional
    public ProjectResponse createProject(Long ownerId, CreateProjectRequest request) {
        /**
         * 관리자 존재 검증
         */
        request.getAdminIds().stream().distinct().forEach(adminId -> {
            if (!userRepository.existsById(adminId)) {
                throw new IllegalArgumentException("존재하지 않는 회원입니다. adminId=" + adminId);
            }
        });

        Project project = Project.create(
                request.getTitle(),
                ownerId,
                request.getSheetUrl(),
                request.getStartAt(),
                request.getEndAt());

        // 연관관계 설정
        request.getAdminIds().stream().map(id -> ProjectAdmin.create(project, id)).forEach(project::addAdmin);

        Project savedProject = projectRepository.save(project);

        // 스프레드 시트 연동

        // 7. 응답 DTO로 변환하여 반환
        return ProjectResponse.from(savedProject);
    }

    /**
     * 이메일로 관리자(회원) 존재 여부 확인
     * 
     * 프로젝트 생성 전에 프론트에서 이메일을 입력받아
     * 해당 이메일로 가입된 회원이 있는지 확인합니다.
     * 
     * DDD 관점에서:
     * - 도메인 Repository(UserRepository)를 통해 회원 조회
     * - 애플리케이션 계층에서 도메인 모델을 DTO로 변환하여 반환
     * 
     * @param email 확인할 이메일
     * @return 관리자 검증 결과 (존재 여부, 유저 정보 또는 에러 메시지)
     */

    @Transactional(readOnly = true)
    public AdminCheckResponse checkAdminByEmail(String email) {
        // 도메인 Repository를 통해 회원 조회
        // UserRepository는 도메인 계층 인터페이스이므로 기술 의존성이 없습니다.
        return userRepository.findByEmail(email)
                .map(user -> AdminCheckResponse.success(
                        user.getId(),
                        user.getNickname(),
                        user.getEmail()))
                .orElse(AdminCheckResponse.notFound(email));
    }
}
