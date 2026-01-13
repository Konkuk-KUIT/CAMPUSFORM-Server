package com.campusform.server.recruiting.domain.model.interview.availability;

import com.campusform.server.recruiting.domain.model.interview.setup.InterviewDay;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * 지원자 면접 가능 슬롯 Entity
 * 제출된 지원자 면접 가능 슬롯을 관리합니다.
 */
@Entity
@Table(name = "interviewee_availability_slots",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_applicant_day", columnNames = {"applicant_id", "interview_day_id"}),
           @UniqueConstraint(name = "uk_day_time", columnNames = {"interview_day_id", "start_time"})
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntervieweeAvailabilitySlot {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 지원자 ID (다른 애그리거트이므로 ID만 저장)
     */
    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;

    /**
     * 면접 일자 (동일 애그리거트 내부이므로 참조로 관계 설정)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_day_id", nullable = false)
    private InterviewDay interviewDay;

    /**
     * 시작 시간 (end_time은 start_time + slot_duration_min)
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
}
