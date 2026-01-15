package com.campusform.server.project.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campusform.server.project.application.dto.ColumnInfo;
import com.campusform.server.project.application.dto.request.CreateProjectRequest;
import com.campusform.server.project.application.dto.response.AdminCheckResponse;
import com.campusform.server.project.application.dto.response.ProjectResponse;
import com.campusform.server.project.application.service.ProjectService;
import com.campusform.server.project.application.service.SpreadsheetService;

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
    private final SpreadsheetService spreadsheetService;

    /**
     * 새 프로젝트 생성
     * 
     * @param request 프로젝트 생성 요청
     * @param ownerId 프로젝트 소유자 ID (실제로는 SecurityContext에서 가져옴)
     * @return 생성된 프로젝트 정보
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @RequestParam Long ownerId) {

        ProjectResponse response = projectService.createProject(ownerId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * 스프레드 헤더 조회 for 1대1 드롭다운 매핑
     * 
     * @param sheetUrl 스프레드시트 URL
     * @return 컬럼명 리스트
     */
    @GetMapping("/spreadsheet/headers")
    public ResponseEntity<List<ColumnInfo>> getSpreadsheetHeaders(@RequestParam String sheetUrl) {
        List<ColumnInfo> response = spreadsheetService.getHeaders(sheetUrl);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트 생성 전에 관리자(회원) 이메일로 추가 가능한지(유효한지) 검증
     * 
     * 존재하면 어드민 정보 내려주고, 존재하지 않으면 실패 사유 전달
     * 
     * 존재하면 유저 정보를 반환하고, 존재하지 않으면 에러 메시지를 포함한 응답을 반환합니다.
     * 
     * @param email 확인할 이메일
     * @return 관리자 검증 결과 (존재 여부, 유저 정보 또는 에러 메시지)
     */
    @GetMapping("/admins/check-available")
    public ResponseEntity<AdminCheckResponse> checkAdminEmailBeforeCreate(@RequestParam String email) {
        AdminCheckResponse response = projectService.checkAdminByEmail(email);

        // 회원이 존재하면 200 OK, 존재하지 않으면 404 Not Found
        if (response.isExists()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}