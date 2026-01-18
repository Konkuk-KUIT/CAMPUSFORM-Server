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

    // [기존 코드] 결과 발표 (상태 변경)
    @Transactional
    public void announceResults(ResultAnnouncementRequest request){
        List<Applicant> applicants = applicantRepository.findAllById(request.applicantIds());
        for (Applicant applicant : applicants){
            applicant.updateApplicantStatus(request.status());
        }
        applicantRepository.saveAll(applicants); // save -> saveAll이 더 성능이 좋습니다
    }

    // [추가 코드] 1.6.1 합불자 명단 및 통계 조회
    public ResultListResponse getResults(Long projectId, String stage, String status) {
        // 1. DB에서 조건에 맞는 지원자들 조회
        List<Applicant> applicants = applicantRepository.findByProjectIdAndStageAndStatus(projectId, stage, status);

        // 2. 통계 계산 (전체 지원자 수, 경쟁률 등)
        // ... 통계 로직 ...

        // 3. DTO로 변환해서 리턴
        return ResultListResponse.builder()
                // ... 데이터 채우기
                .build();
    }
    // 1.6.2 템플릿 저장
    @Transactional
    public void saveTemplate(Long projectId, String stage, SmsTemplateSaveRequest request) {
        // 1. 없으면 생성, 있으면 가져오기
        MessageTemplate template = templateRepository.findById(projectId)
                .orElseGet(() -> templateRepository.save(MessageTemplate.createEmpty(projectId)));

        // 2. 내용 업데이트 (엔티티 메서드 활용)
        template.updateTemplate(stage, String.valueOf(request.getStatus()), request.getContent());

        // Dirty Checking으로 자동 저장됨 (Transaction 종료 시)
    }

    // 1.6.3 미리보기 (특정 1인)
    @Transactional(readOnly = true)
    public SmsPreviewResponse getPreview(Long projectId, Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("지원자가 없습니다."));

        // 1. 현재 지원자의 상태에 맞는 템플릿 찾기
        // (주의: 미리보기는 현재 합격 상태인 사람에게 보낼 메시지를 보는 것이므로 status를 추론해야 함)
        // 일단 기본적으로 서류 합격 템플릿을 가져온다고 가정하거나, 요청에서 stage를 받아야 정확함.
        // 여기선 '서류 합격' 상태라고 가정하고 코드를 짭니다.
        String templateContent = templateRepository.findById(projectId)
                .map(MessageTemplate::getTemplateDocumentPass) // 편의상 서류 합격 사용
                .orElse("템플릿이 없습니다.");

        // 2. 치환
        String finalContent = templateContent
                .replace("@이름", applicant.getName());

        SmsPreviewResponse.PreviewMessage message = SmsPreviewResponse.PreviewMessage.builder()
                .applicantId(applicant.getId())
                .name(applicant.getName())
                .phoneNumber(applicant.getPhone()) // Entity 필드명: phone
                .info(applicant.getSchool() + " / " + applicant.getMajor() + " / " + applicant.getPosition())
                .content(finalContent)
                .build();

        return SmsPreviewResponse.builder()
                .count(1)
                .messages(List.of(message))
                .build();
    }
}
