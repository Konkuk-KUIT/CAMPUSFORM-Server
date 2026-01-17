package com.campusform.server.recruiting.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.repository.ApplicantRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * ApplicantRepository 구현체
 * 
 * Spring Data JPA에 작업을 위임합니다.
 * 향후 Querydsl이 필요하면 여기에 추가할 수 있습니다.
 */
@Repository
@RequiredArgsConstructor
public class ApplicantRepositoryImpl implements ApplicantRepository {

    private final ApplicantJpaRepository applicantJpaRepository;

    @Override
    public void save(Applicant applicant) {
        applicantJpaRepository.save(applicant);
    }

    @Override
    public List<com.campusform.server.recruiting.domain.model.applicant.Applicant> findAllById(List<Long> longs) {
        return List.of();
    }

    @Override
    public void save(List<com.campusform.server.recruiting.domain.model.applicant.Applicant> applicants) {

    }
}
