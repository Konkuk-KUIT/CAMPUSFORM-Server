package com.campusform.server.recruiting.application.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 필수 면접관 설정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequiredInterviewersRequest {

    /**
     * 필수 면접관 ID 목록
     */
    private List<Long> adminIds;
}
