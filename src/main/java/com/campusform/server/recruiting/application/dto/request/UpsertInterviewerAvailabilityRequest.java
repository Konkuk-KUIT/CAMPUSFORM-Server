package com.campusform.server.recruiting.application.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Step2: 특정 면접관의 가능 시간 저장 요청
 * 
 * 날짜별로 30분 단위 블록 목록을 받습니다.
 * 기존 데이터는 덮어쓰기 방식으로 처리됩니다.
 */
@Getter
@NoArgsConstructor // Jackson 역직렬화를 위해 기본 생성자 필요
@AllArgsConstructor
public class UpsertInterviewerAvailabilityRequest {

    private List<DayAvailability> availabilities;

    /**
     * 날짜별 가능 시간 블록 목록
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayAvailability {

        private LocalDate date;

        // 30분 단위 시작 시간 목록
        private List<LocalTime> startTimes;
    }
}
