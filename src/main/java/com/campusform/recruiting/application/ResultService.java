package com.campusform.recruiting.application;

import com.campusform.recruiting.application.dto.ResultAnnouncementRequest;
import com.campusform.recruiting.application.port.SmsSender;
import com.campusform.recruiting.domain.applicant.Applicant;
import com.campusform.recruiting.domain.applicant.ApplicantRepository;
import com.campusform.recruiting.domain.applicant.EvaluationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ApplicantRepository applicantRepository;
    private final SmsSender smsSender;

    private static final String PASS_TEMPLATE = "[CAMPUS:FORM] 안녕하세요 @이름님! 축하드립니다. 서류 전형에 합격하셨습니다.";
    private static final String FAIL_TEMPLATE = "[CAMPUS:FORM] 안녕하세요 @이름님. 아쉽게도 이번 전형에서는 모시지 못하게 되었습니다.";

    @Transactional
    public void announceResults(ResultAnnouncementRequest request){
        // 1. 대상 지원자 일괄 조회
        List<Applicant> applicants = applicantRepository.findAllById(request.applicantIds());

        //2. 상태 변경 (도메인 로직 실행)
        for (Applicant applicant : applicants){
            applicant.updateEvaluationStatus(request.status());
        }

        //3. 저장 ( 이때 update 쿼리가 나가고 registerEvent 했던 이벤트들이 발행된다.)
        sendNotificationToApplicants(applicants);
    }

    // 문자 발송 로직 분리
    private void sendNotificationToApplicants(List<Applicant> applicants) {
        // 3-1. 합격/불합격자 분류 (Stream groupingBy 사용)
        // 결과 예시: {PASSED=[철수, 영희], FAILED=[길동]}
        Map<EvaluationStatus, List<Applicant>> groupedApplicants = applicants.stream()
                .collect(Collectors.groupingBy(Applicant::getDocumentStatus));

        // 3-2. 합격자 발송
        List<Applicant> passedApplicants = groupedApplicants.get(EvaluationStatus.PASSED);
        if (passedApplicants != null) {
            sendBulkSms(passedApplicants, PASS_TEMPLATE);
        }

        // 3-3. 불합격자 발송
        List<Applicant> failedApplicants = groupedApplicants.get(EvaluationStatus.FAILED);
        if (failedApplicants != null) {
            sendBulkSms(failedApplicants, FAIL_TEMPLATE);
        }
    }

    // 템플릿 적용 및 실제 발송 (공통 메서드)
    private void sendBulkSms(List<Applicant> targets, String template) {
        for (Applicant applicant : targets) {
            // 4. 템플릿 치환 (@이름 -> 실제이름)
            String message = template.replace("@이름", applicant.getName());

            // 5. 발송 (전화번호가 없으면 건너뛰는 방어 로직 추가 가능)
            if (applicant.getPhone() != null) {
                smsSender.sendSms(applicant.getPhone(), message);
            }
        }
    }

}
