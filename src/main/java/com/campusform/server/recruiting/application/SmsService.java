package com.campusform.server.recruiting.application;

import com.campusform.server.recruiting.application.dto.ResultAnnouncementRequest;
import com.campusform.server.recruiting.application.dto.response.ResultListResponse;
import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsService {
    //문자 메시지 내용, 템플릿, 발송 서비스
    private final ApplicantRepository applicantRepository;

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

}
