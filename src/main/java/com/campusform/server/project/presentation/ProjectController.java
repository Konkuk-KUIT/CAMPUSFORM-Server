package com.campusform.server.project.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campusform.server.project.application.dto.request.CreateProjectRequest;
import com.campusform.server.project.application.dto.response.ProjectResponse;
import com.campusform.server.project.application.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 현재는 간단히 ownerId를 파라미터로 받지만, 실제로는
 * SecurityContext에서 현재 로그인한 사용자 정보를 가져와야 합니다.
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 새 프로젝트 생성
     * 
     * @param request 프로젝트 생성 요청
     * @param ownerId 프로젝트 소유자 ID (실제로는 SecurityContext에서 가져옴)
     * @return 생성된 프로젝트 정보
     * 
     *         1. 요청 도착
     *         2. JSON -> CreateProjectRequest 객체 변환
     *         3. BeanValidation 검증
     *         - 성공 -> 컨트롤러 메서드 실행
     *         - 실패 -> MethodArgumentNotValidException 발생
     *         4. ProjectService.createProject 호출
     *         5. 결과 반환 (201 Created)
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @RequestParam Long ownerId) {

        ProjectResponse response = projectService.createProject(ownerId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}