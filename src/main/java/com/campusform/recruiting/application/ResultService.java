package com.campusform.recruiting.application;

import com.campusform.recruiting.domain.applicant.Applicant;
import com.campusform.recruiting.domain.applicant.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ApplicantRepository applicantRepository;

    @Transactional
    public void announceResults(ResultAnnouncementRequest request){
        // 1. 대상 지원자 일괄 조회
        List<Applicant> applicants = applicantRepository.findAllById(request.applicantIds());

        //2. 상태 변경 (도메인 로직 실행)
        for (Applicant applicant : applicants){
            applicant.updateEvaluationStatus(request.status());
        }

        //3. 저장 ( 이때 update 쿼리가 나가고 registerEvent 했던 이벤트들이 발행된다.)
        applicantRepository.saveAll(applicants);
    }

}
