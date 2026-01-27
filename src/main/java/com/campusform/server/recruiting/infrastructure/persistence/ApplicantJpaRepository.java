package com.campusform.server.recruiting.infrastructure.persistence;

import com.campusform.server.recruiting.domain.model.applicant.value.ApplicantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusform.server.recruiting.domain.model.applicant.Applicant;

import java.util.List;

/**
 * Spring Data JPA를 위한 Applicant Repository
 * 
 * 기본 CRUD 메서드를 제공합니다.
 */
@Repository
public interface ApplicantJpaRepository extends JpaRepository<Applicant, Long> {
    // JPA가 이름만 보고 자동으로 쿼리를 만들어줌.
    long countByProjectId(Long projectId);

    List<Applicant> findByProjectIdAndDocumentStatus(Long projectId, ApplicantStatus documentStatus);

    List<Applicant> findByProjectIdAndInterviewStatus(Long projectId, ApplicantStatus interviewStatus);
}
