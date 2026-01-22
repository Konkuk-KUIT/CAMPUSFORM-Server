package com.campusform.server.recruiting.presentation;


import com.campusform.server.recruiting.application.ResultService;
import com.campusform.server.recruiting.application.SmsService;
import com.campusform.server.recruiting.application.dto.request.ResultAnnouncementRequest;
import com.campusform.server.recruiting.application.dto.request.SmsTemplateSaveRequest;
import com.campusform.server.recruiting.application.dto.response.ResultListResponse;
import com.campusform.server.recruiting.application.dto.response.SmsPreviewResponse;
import com.campusform.server.recruiting.domain.model.applicant.value.ApplicantStatus;
import com.campusform.server.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}")
public class ResultController {
    private final ResultService resultService;
    private final SmsService smsService;

    //1.6.1 서류, 면접 합불자 명단 조회
    @GetMapping("/results")
    public ApiResponse<ResultListResponse> getResultList(
            @PathVariable Long projectId,
            @RequestParam String stage, //document
            @RequestParam String status //pass
    ){
        System.out.println(">>> 컨트롤러 도착함! ID: " + projectId);
        ApplicantStatus statusEnum = ApplicantStatus.valueOf(status.toUpperCase());
        ResultListResponse response=resultService.getResults(projectId,stage,statusEnum);
        return ApiResponse.success(response);
    }
    // 1.6.2 문자 템플릿 저장
    @PostMapping("/sms/templates")
    public ApiResponse<Void> saveSmsTemplate(
            @PathVariable Long projectId,
            @RequestParam String stage,
            @RequestBody SmsTemplateSaveRequest request
    ) {
        smsService.saveTemplate(projectId, stage, request);
        return ApiResponse.success("템플릿을 저장했습니다.",null);
    }

    // 1.6.3 개인별 문자메시지 미리보기
    // 주의: 명세서 URL에는 applicantId가 있지만, 응답 Body는 전체 목록(List)을 반환하므로
    // 실제로는 '특정 1명'보다는 '해당 상태의 모든 지원자 미리보기' 기능일 가능성이 큽니다.
    // 일단 URL 명세대로 적습니다.
    @GetMapping("/applicants/{applicantId}/sms/preview")
    public ApiResponse<SmsPreviewResponse> getSmsPreview(
            @PathVariable Long projectId,
            @PathVariable Long applicantId
    ) {
        SmsPreviewResponse response = smsService.getPreview(projectId, applicantId);
        return ApiResponse.success("개인별 메시지 생성 성공", response);
    }

    @PostMapping("/announce")
    public ResponseEntity<Void> announceResult(@RequestBody ResultAnnouncementRequest request){
        // 1. 만약 요청 데이터가 이상하면 여기서 컷! (Validation)
        if (request.applicantIds() == null || request.applicantIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 2. 서비스 호출 (방아쇠 당기기)
        resultService.announceResults(request);

        // 3. 클라이언트에게 "성공했어!"라고 HTTP 상태 코드(200)로 응답
        return ResponseEntity.ok().build();

    }
}
