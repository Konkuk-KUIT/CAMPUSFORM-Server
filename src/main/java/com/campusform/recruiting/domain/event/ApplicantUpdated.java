package com.campusform.recruiting.domain.event;

import com.campusform.recruiting.domain.applicant.EvaluationStatus;

// 변경된 상태와 문자 발송에 필요한 정보(전화번호, 이름 등)
public record ApplicantUpdated(
        Long applicantId,
        String applicantName,
        String applicantPhone,
        EvaluationStatus status
){
}
