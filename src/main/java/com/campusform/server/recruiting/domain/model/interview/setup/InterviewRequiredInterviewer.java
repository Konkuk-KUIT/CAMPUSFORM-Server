package com.campusform.server.recruiting.domain.model.interview.setup;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 필수 면접관 Entity
 * 필수 면접관 관리를 담당합니다.
 */
@Entity
@Table(name = "interview_required_interviewers",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_project_admin", columnNames = {"project_id", "admin_id"})
       },
       indexes = @Index(name = "idx_project_id", columnList = "project_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class InterviewRequiredInterviewer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    /**
     * 필수 면접관 여부
     */
    @Column(nullable = false)
    private Boolean required = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
