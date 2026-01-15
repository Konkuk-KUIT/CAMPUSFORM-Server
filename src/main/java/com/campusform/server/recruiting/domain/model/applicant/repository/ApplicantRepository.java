package com.campusform.server.recruiting.domain.model.applicant.repository;

import com.campusform.server.recruiting.domain.model.applicant.Applicant;

public interface ApplicantRepository {

    void save(Applicant applicant);
}
