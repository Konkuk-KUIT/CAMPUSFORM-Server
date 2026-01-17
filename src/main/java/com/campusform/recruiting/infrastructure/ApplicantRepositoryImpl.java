package com.campusform.recruiting.infrastructure;

import com.campusform.recruiting.domain.applicant.Applicant;
import com.campusform.recruiting.domain.applicant.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public interface ApplicantRepositoryImpl implements ApplicantRepository {
    private final ApplicantRepository applicantJpaRepository;

    @Override
    public void save(Applicant applicant){
        applicantJpaRepository.save(applicant);
    }
}
