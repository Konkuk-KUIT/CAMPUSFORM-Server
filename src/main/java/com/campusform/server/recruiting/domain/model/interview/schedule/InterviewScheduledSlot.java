package com.campusform.server.recruiting.domain.model.interview.schedule;

import com.campusform.server.recruiting.domain.model.interview.setup.InterviewDay;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * 배정된 면접 슬롯 Entity
 */
@Entity
@Table(name = "interview_scheduled_slots",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_project_day_time", columnNames = {"project_id", "interview_day_id", "start_time"})
       },
       indexes = @Index(name = "idx_interview_day_time", columnList = "interview_day_id, start_time"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewScheduledSlot {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 동일 애그리거트 -> 연관 아닌 참조로 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_day_id", nullable = false)
    private InterviewDay interviewDay;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
}
