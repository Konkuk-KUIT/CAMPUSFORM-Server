package com.campusform.server.project.domain.model.projectadmin;

import com.campusform.server.project.domain.model.projectadmin.value.ProjectRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 프로젝트 관리자 Entity
 * 프로젝트별 ADMIN 관리를 담당합니다.
 */
@Entity
@Table(name = "project_admins",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_project_admin", columnNames = {"project_id", "admin_id"})
       },
       indexes = @Index(name = "idx_project_id", columnList = "project_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProjectAdmin {

    @Id
    @GeneratedValue
    private Long id;

    // 다른 어그리거트 -> 참조 아닌 연관으로 관계 설정
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 다른 어그리거트 -> 참조 아닌 연관으로 관계 설정
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role = ProjectRole.ADMIN;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
