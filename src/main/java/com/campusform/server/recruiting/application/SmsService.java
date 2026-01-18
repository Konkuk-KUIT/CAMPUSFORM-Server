package com.campusform.server.recruiting.application;

import com.campusform.server.recruiting.application.dto.request.ResultAnnouncementRequest;
import com.campusform.server.recruiting.application.dto.request.SmsTemplateSaveRequest;
import com.campusform.server.recruiting.application.dto.response.ResultListResponse;
import com.campusform.server.recruiting.application.dto.response.SmsPreviewResponse;
import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.model.message.MessageTemplate;
import com.campusform.server.recruiting.domain.repository.ApplicantRepository;
import com.campusform.server.recruiting.domain.repository.MessageTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsService {
    //문자 메시지 내용, 템플릿, 발송 서비스
    private final ApplicantRepository applicantRepository;
    private final MessageTemplateRepository templateRepository;


    /**
     * 문자 관련 로직만
     * 1.6.2 템플릿 저장
     * @param projectId
     * @param stage
     * @param request
     */
    @Transactional
    public void saveTemplate(Long projectId, String stage, SmsTemplateSaveRequest request) {
        // 1. 없으면 생성, 있으면 가져오기
        MessageTemplate template = templateRepository.findById(projectId)
                .orElseGet(() -> templateRepository.save(MessageTemplate.createEmpty(projectId)));

        // 2. 내용 업데이트 (엔티티 메서드 활용)
        template.updateTemplate(stage, String.valueOf(request.getStatus()), request.getContent());

        // Dirty Checking으로 자동 저장됨 (Transaction 종료 시)
    }

    /**
     * [1.6.3] 개인별 문자메시지 미리보기
     */
    @Transactional(readOnly = true)
    public SmsPreviewResponse getPreview(Long projectId, Long applicantId) {
        // 1. 지원자 조회
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("지원자가 없습니다."));

        // 2. 템플릿 조회 (일단 서류 합격 템플릿을 기본으로 가져옴 - 로직에 따라 변경 가능)
        String templateContent = templateRepository.findById(projectId)
                .map(MessageTemplate::getTemplateDocumentPass)
                .orElse("저장된 템플릿이 없습니다.");

        // 3. 변수 치환 (@이름 -> 실제 이름)
        String finalContent = templateContent.replace("@이름", applicant.getName());

        // 4. 응답 DTO 생성
        SmsPreviewResponse.PreviewMessage message = SmsPreviewResponse.PreviewMessage.builder()
                .applicantId(applicant.getId())
                .name(applicant.getName())
                .phoneNumber(applicant.getPhone())
                .info(makeInfoString(applicant)) // 정보 문자열 만드는 헬퍼 메서드 사용
                .content(finalContent)
                .build();

        return SmsPreviewResponse.builder()
                .count(1)
                .messages(List.of(message))
                .build();
    }
    // 미리보기용 정보 문자열 생성 ("학교 / 전공 / 지원분야")
    private String makeInfoString(Applicant applicant) {
        return (applicant.getSchool() != null ? applicant.getSchool() : "-") + " / " +
                (applicant.getMajor() != null ? applicant.getMajor() : "-") + " / " +
                (applicant.getPosition() != null ? applicant.getPosition() : "-");
    }
}
