package com.campusform.server.recruiting.application.service;

import com.campusform.server.recruiting.application.dto.response.ApplicantListResponse;
import com.campusform.server.recruiting.application.dto.response.ApplicantResponse;
import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.model.applicant.value.ApplicantStatus;
import com.campusform.server.recruiting.domain.model.applicant.value.StageStatus;
import com.campusform.server.recruiting.domain.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public ApplicantListResponse getApplicants(Long projectId, String sort, StageStatus stage) {
        long total = applicantRepository.countByProjectId(projectId);
        long pending = 0;
        long pass = 0;
        long fail = 0;
        // 2. 단계(Stage)에 따라 "누구의 합격 상태"를 셀지 결정 (if문 분기)
        if (stage == StageStatus.DOCUMENT) {
            // [서류 탭] -> documentStatus 기준 카운트
            pending = applicantRepository.countByProjectIdAndDocumentStatus(projectId, ApplicantStatus.HOLD);
            pass = applicantRepository.countByProjectIdAndDocumentStatus(projectId, ApplicantStatus.PASS);
            fail = applicantRepository.countByProjectIdAndDocumentStatus(projectId, ApplicantStatus.FAIL);
        }
        else if (stage == StageStatus.INTERVIEW) {
            // [면접 탭] -> interviewStatus 기준 카운트
            pending = applicantRepository.countByProjectIdAndInterviewStatus(projectId, ApplicantStatus.HOLD);
            pass = applicantRepository.countByProjectIdAndInterviewStatus(projectId, ApplicantStatus.PASS);
            fail = applicantRepository.countByProjectIdAndInterviewStatus(projectId, ApplicantStatus.FAIL);
        }
        else {
            // 예외 처리: 단계가 없으면 통계를 낼 수 없음
            throw new IllegalArgumentException("서류 또는 면접 단계를 반드시 선택해야 합니다.");
        }

        List<Applicant> applicants;

        if(stage !=null){
            applicants = applicantRepository.findByProjectIdAndStage(projectId, stage);
            switch (sort) {
                case "name_desc": // 2. 이름 내림차순 (하파타순)
                    applicants = applicantRepository.findByProjectIdOrderByNameDesc(projectId);
                    break;
                case "bookmark": // 3. 찜한 순 (찜한거 위로, 나머지는 최신순)
                    applicants = applicantRepository.findByProjectIdOrderByBookmarkedDescIdDesc(projectId);
                    break;
                default: // 1. 이름 오름차순 (가나다순)
                    applicants = applicantRepository.findByProjectIdOrderByNameAsc(projectId);
                    break;
            }
        }
        else{
            throw new IllegalArgumentException("서류 또는 면접 단계를 반드시 선택해야 합니다.");
        }
        // DTO로 변환하기
        List<ApplicantResponse> applicantDtos = applicants.stream()
                .map(applicant -> ApplicantResponse.builder()
                        .id(applicant.getId())
                        .name(applicant.getName())
                        .major(applicant.getMajor()) // 학과
                        .phone(applicant.getPhone()) // 전화번호
                        .bookmarked(applicant.getBookmarked()) // ★ 찜 여부
                        // 필요한 필드가 더 있다면 여기에 계속 추가
                        //.email(applicant.getEmail())
                        .build())
                .toList();

        // 4. [추가] 최종 응답 객체(ApplicantListResponse)로 감싸서 반환
        return ApplicantListResponse.builder()
                .status(ApplicantListResponse.ApplicantStatus.builder()
                        .totalCount(total)
                        .pendingCount(pending)
                        .passCount(pass)
                        .failCount(fail)
                        .build())
                .applicants(applicantDtos)
                .build();
    }
    // 찜하기 토글
    @Transactional
    public void Bookmark(Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("지원자가 존재하지 않습니다."));
        applicant.Bookmark();
    }
}
