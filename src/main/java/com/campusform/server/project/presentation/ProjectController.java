package com.campusform.server.project.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 프로젝트 생성
     * 
     * @param request 생성 폼 데이터
     * @param ownerId 프로젝트 소유자 ID (Identity Context에서 조회)
     * @return 생성된 프로젝트 정보
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @RequestParam Long ownerId) {
        ProjectResponse response = projectService.createProject(ownerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 서류 단계 종료 및 프로젝트 종료
     * 
     * 서류 단계를 완료하고 프로젝트를 종료합니다.
     * 프로젝트 상태가 DOCUMENT_LOCKED인 경우에만 가능합니다.
     */
    @PatchMapping("/{projectId}/complete-document")
    public ResponseEntity<ProjectResponse> completeDocument(@PathVariable Long projectId) {
        ProjectResponse response = projectService.completeDocument(projectId);
        return ResponseEntity.ok(response);
    }

    /**
     * 면접 단계 종료 및 프로젝트 종료
     * 
     * 서류 단계와 면접 단계를 모두 완료하고 프로젝트를 종료합니다.
     * 프로젝트 상태가 INTERVIEW_LOCKED인 경우에만 가능합니다.
     */
    @PatchMapping("/{projectId}/complete-all")
    public ResponseEntity<ProjectResponse> completeAll(@PathVariable Long projectId) {
        ProjectResponse response = projectService.completeAll(projectId);
        return ResponseEntity.ok(response);
    }
}