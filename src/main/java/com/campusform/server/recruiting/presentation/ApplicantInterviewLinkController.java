package com.campusform.server.recruiting.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campusform.server.identity.application.service.AuthService;
import com.campusform.server.recruiting.application.dto.request.UpdateApplicantLinkConfigRequest;
import com.campusform.server.recruiting.application.dto.response.ApplicantInterviewLinkConfigResponse;
import com.campusform.server.recruiting.application.dto.response.ApplicantInterviewLinkResponse;
import com.campusform.server.recruiting.application.dto.response.InterviewSlotListResponse;
import com.campusform.server.recruiting.application.dto.response.SlotApplicantListResponse;
import com.campusform.server.recruiting.application.service.ApplicantInterviewLinkService;
import com.campusform.server.recruiting.application.service.SlotApplicantService;

import lombok.RequiredArgsConstructor;

/**
 * 스마트 시간표 설정 - 3. 지원자 면접 가능 시간 모집 (Owner용)
 */
@RestController
@RequestMapping("/api/recruiting/projects")
@RequiredArgsConstructor
public class ApplicantInterviewLinkController {

    private final ApplicantInterviewLinkService applicantInterviewLinkService;
    private final SlotApplicantService slotApplicantService;

    private final AuthService authService;

    /**
     * 지원자 링크 조회
     * 
     * <pre>
     * 응답 예시:
     * {
     *   "token": "550e8400-e29b-41d4-a716-446655440000",
     *   "url": "/submit?token=550e8400-e29b-41d4-a716-446655440000"
     * }
     * </pre>
     */
    @GetMapping("/{projectId}/investigation-link")
    public ResponseEntity<ApplicantInterviewLinkResponse> getApplicantLink(
            @PathVariable Long projectId,
            Authentication authentication) {
        Long userId = authService.extractUserId(authentication);
        ApplicantInterviewLinkResponse response = applicantInterviewLinkService.getApplicantLink(projectId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 지원자 페이지 설정 조회
     * 
     * <pre>
     * 응답 예시:
     * {
     *   "enabled": true,
     *   "guidanceText": "면접 가능 시간을 선택해주세요."
     * }
     * </pre>
     */
    @GetMapping("/{projectId}/investigation-link/config")
    public ResponseEntity<ApplicantInterviewLinkConfigResponse> getApplicantLinkConfig(
            @PathVariable Long projectId,
            Authentication authentication) {
        Long userId = authService.extractUserId(authentication);
        ApplicantInterviewLinkConfigResponse response = applicantInterviewLinkService
                .getApplicantLinkConfig(projectId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 지원자 페이지 설정 수정
     * 
     * <pre>
     * 요청 예시:
     * {
     *   "enabled": true,
     *   "guidanceText": "면접 가능 시간을 선택해주세요."
     * }
     * </pre>
     */
    @PutMapping("/{projectId}/investigation-link/config")
    public ResponseEntity<ApplicantInterviewLinkConfigResponse> updateApplicantLinkConfig(
            @PathVariable Long projectId,
            Authentication authentication,
            @RequestBody UpdateApplicantLinkConfigRequest request) {
        Long userId = authService.extractUserId(authentication);
        ApplicantInterviewLinkConfigResponse response = applicantInterviewLinkService
                .updateApplicantLinkConfig(projectId, userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 면접 슬롯 목록 조회
     * 
     * <pre>
     * 응답 예시:
     * {
     *   "summaries": [
     *     {
     *       "date": "2024-07-01",
     *       "slots": [
     *         {
     *           "startTime": "10:00",
     *           "endTime": "10:20",
     *           "availableInterviewerCount": 2
     *         },
     *         {
     *           "startTime": "10:25",
     *           "endTime": "10:45",
     *           "availableInterviewerCount": 1
     *         }
     *       ]
     *     }
     *   ]
     * }
     * </pre>
     */
    @GetMapping("/{projectId}/interview-slots")
    public ResponseEntity<InterviewSlotListResponse> getInterviewSlotList(
            @PathVariable Long projectId,
            Authentication authentication) {
        Long userId = authService.extractUserId(authentication);
        InterviewSlotListResponse response = applicantInterviewLinkService.getInterviewSlotList(projectId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 슬롯별 지원자 목록 조회
     * 
     * 프로젝트의 모든 날짜의 모든 슬롯에 제출한 지원자들의 정보를 조회합니다.
     * 
     * <pre>
     * 응답 예시:
     * {
     *   "summaries": [
     *     {
     *       "date": "2024-07-01",
     *       "slots": [
     *         {
     *           "startTime": "10:00",
     *           "endTime": "10:20",
     *           "applicants": [
     *             {
     *               "applicantId": 1,
     *               "name": "홍길동",
     *               "school": "서울대학교",
     *               "major": "컴퓨터공학과",
     *               "position": "백엔드"
     *             },
     *             {
     *               "applicantId": 2,
     *               "name": "김철수",
     *               "school": "연세대학교",
     *               "major": "정보시스템학과",
     *               "position": null
     *             }
     *           ]
     *         },
     *         {
     *           "startTime": "10:25",
     *           "endTime": "10:45",
     *           "applicants": []
     *         }
     *       ]
     *     }
     *   ]
     * }
     * </pre>
     * 
     * @param projectId 프로젝트 ID
     */
    @GetMapping("/{projectId}/interview-slots/applicants")
    public ResponseEntity<SlotApplicantListResponse> getAllApplicantsBySlots(
            @PathVariable Long projectId,
            Authentication authentication) {
        authService.extractUserId(authentication);

        SlotApplicantListResponse response = slotApplicantService.getAllApplicantsBySlots(projectId);
        return ResponseEntity.ok(response);
    }
}
