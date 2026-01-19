package com.campusform.server.recruiting.application.dto.request;

import com.campusform.server.recruiting.domain.model.applicant.value.ApplicantStatus;

import java.util.List;

public record ResultAnnouncementRequest(
        List<Long> applicantIds, // 처리할 지원자 ID 목록
        ApplicantStatus status // PASSED or FAILED
) {
}
