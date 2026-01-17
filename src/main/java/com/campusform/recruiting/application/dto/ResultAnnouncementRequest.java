package com.campusform.recruiting.application.dto;

import com.campusform.recruiting.domain.applicant.EvaluationStatus;

import java.util.List;

public record ResultAnnouncementRequest(
        List<Long> applicantIds, // 처리할 지원자 ID 목록
        EvaluationStatus status // PASSED or FAILED
) {
}
