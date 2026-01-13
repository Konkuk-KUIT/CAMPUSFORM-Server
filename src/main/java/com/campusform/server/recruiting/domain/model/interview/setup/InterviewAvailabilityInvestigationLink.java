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
 * 지원자 면접 가능 시간 조사 링크 Entity
 * 지원자 면접 가능 시간 모집을 위한 공개 링크를 관리합니다.
 */
@Entity
@Table(name = "interview_availability_investigation_links",
       indexes = @Index(name = "idx_project_id", columnList = "project_id", unique = true))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class InterviewAvailabilityInvestigationLink {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "project_id", nullable = false, unique = true)
    private Long projectId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "guidance_text", columnDefinition = "TEXT")
    private String guidanceText;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
