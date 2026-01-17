package com.campusform.recruiting.infrastructure;

import com.campusform.recruiting.domain.applicant.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, Integer> {
}
