package com.campusform.recruiting.domain.applicant;

import java.util.List;

public interface ApplicantRepository {

    void save(Applicant applicant);

    List<Applicant> findAllById(List<Long> longs);

    void saveAll(List<Applicant> applicants);
}
