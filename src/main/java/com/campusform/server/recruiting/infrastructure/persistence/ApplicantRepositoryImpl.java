package com.campusform.server.recruiting.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.model.applicant.repository.ApplicantRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ApplicantRepositoryImpl implements ApplicantRepository {

    private final JpaApplicantRepository jpaApplicantRepository;

    @Override
    public void save(Applicant applicant) {
        jpaApplicantRepository.save(applicant);
    }
}
