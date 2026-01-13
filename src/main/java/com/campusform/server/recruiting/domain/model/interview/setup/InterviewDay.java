package com.campusform.server.recruiting.domain.model.interview.setup;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 면접 일자 Entity
 * 면접 일자 복수 관리를 담당합니다.
 */
@Entity
@Table(name = "interview_days",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_project_interview_date", columnNames = {"project_id", "interview_date"})
       },
       indexes = @Index(name = "idx_project_id", columnList = "project_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewDay {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 프로젝트 ID (Project Context 참조이므로 ID만 저장)
     */
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /**
     * 면접 날짜
     */
    @Column(name = "interview_date", nullable = false)
    private java.time.LocalDate interviewDate;
}
