package com.campusform.server.project.application.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.campusform.server.project.application.dto.ColumnInfo;

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
@Setter
@NoArgsConstructor
public class CreateProjectRequest {

    /**
     * 요청 예시
     * POST /api/projects
     * 
     * {
     * "title": "2024 하계 연구프로젝트",
     * "sheetUrl": "https://docs.google.com/spreadsheets/d/xxxxx",
     * "startAt": "2024-07-01",
     * "endAt": "2024-09-30",
     * "adminIds": [2, 3],
     * "mappings": [
     * { "name": "이름", "index": 0 },
     * { "name": "학교", "index": 1 },
     * { "name": "학과", "index": 2 },
     * { "name": "성별", "index": 3 }
     * ]
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

    /**
     * 스프레드시트 컬럼 매핑 정보
     * 
     * 프로젝트 생성 시 스프레드시트의 컬럼과 표준 항목 간의 매핑 정보를 포함합니다.
     * 필수 항목(이름, 이메일)이 포함되어야 합니다.
     */
    public List<ColumnInfo> mappings;
}
