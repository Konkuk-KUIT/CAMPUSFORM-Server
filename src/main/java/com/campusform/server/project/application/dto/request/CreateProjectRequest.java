package com.campusform.server.project.application.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프로젝트 생성 요청 DTO
 * 클라이언트로부터 받은 프로젝트 생성 정보를 담는 객체입니다.
 */
@Getter
@NoArgsConstructor
public class CreateProjectRequest {

    /**
     * 요청 메시지 예시 (Request body example)
     *
     * {
     * "title": "캠퍼스 폼 개발 프로젝트",
     * "sheetUrl": "https://docs.google.com/spreadsheets/d/xxxxxx",
     * "startAt": "2024-06-01",
     * "endAt": "2024-12-31",
     * "adminIds": [1, 2, 3],
     * "requiredMappings": {
     * "nameIdx": 1,
     * "schoolIdx": -1,
     * "majorId": 3,
     * "genderId": 4,
     * "phoneId": 5,
     * "emailId": 6,
     * "positionId": 7
     * }
     * }
     */

    @NotBlank(message = "프로젝트 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "스프레드시트 URL은 필수입니다.")
    private String sheetUrl;

    @NotNull(message = "모집 시작일은 필수입니다.")
    private LocalDate startAt;

    @NotNull(message = "모집 종료일은 필수입니다.")
    private LocalDate endAt;

    private List<Long> adminIds;

    private RequiredMappings requiredMappings;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequiredMappings {
        private Integer nameIdx;
        private Integer schoolIdx;
        private Integer majorIdx;
        private Integer genderIdx;
        private Integer phoneIdx;
        private Integer emailIdx;
        private Integer positionIdx;
    }
}
