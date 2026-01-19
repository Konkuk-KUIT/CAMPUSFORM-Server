package com.campusform.server.recruiting.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsTemplateSaveRequest {
    // Enum으로 받을지 String으로 받을지는 프로젝트 정책에 따름 (여기선 String 예시)
    @NotBlank
    private String stage;   // "DOCUMENT"

    private Enum status;  // "PASS" or "FAIL"

    @NotBlank(message = "문자 내용은 필수입니다.")
    private String content; // "안녕하세요 [요리퐁]입니다..."
}
