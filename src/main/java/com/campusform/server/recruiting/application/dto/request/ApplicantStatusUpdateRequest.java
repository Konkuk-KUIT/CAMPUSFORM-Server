package com.campusform.server.recruiting.application.dto.request;

import com.campusform.server.recruiting.domain.model.applicant.value.ApplicantStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicantStatusUpdateRequest {
    @NotNull(message = "상태값은 필수입니다.")
    private ApplicantStatus status;
}
