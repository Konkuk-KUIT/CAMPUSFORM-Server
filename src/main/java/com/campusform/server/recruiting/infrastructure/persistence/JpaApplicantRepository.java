package com.campusform.server.recruiting.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusform.server.recruiting.domain.model.applicant.Applicant;

@Repository
public interface JpaApplicantRepository extends JpaRepository<Applicant, Long> {
}
