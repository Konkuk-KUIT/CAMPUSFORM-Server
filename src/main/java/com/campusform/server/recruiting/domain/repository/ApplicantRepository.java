package com.campusform.server.recruiting.domain.repository;

import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.model.applicant.value.ApplicantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 도메인 계층의 Repository 인터페이스
 * 
 * 특정 기술에 의존하지 않고 도메인 관점에서 인터페이스를 정의합니다.
 * 구현체는 infrastructure 계층에서 제공됩니다.
 * 이 코드는 "도메인 영역의 요청"을 받아서 "스프링 JPA(DB)"에게 토스해주는 역할을 완벽하게 수행합니다.
 */
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Object save(Applicant applicant);

    // 프로젝트의 전체 지원자 수 (통계용)
    long countByProjectId(Long projectId);

    // 1. 서류 단계 상태로 조회
    List<Applicant> findByProjectIdAndDocumentStatus(Long projectId, ApplicantStatus status);

    // 2. 면접 단계 상태로 조회
    List<Applicant> findByProjectIdAndInterviewStatus(Long projectId, ApplicantStatus status);

    List<Applicant> findByProjectIdAndStageAndStatus(Long projectId, String stage, String status);
}
