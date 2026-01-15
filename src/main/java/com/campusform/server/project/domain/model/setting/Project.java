package com.campusform.server.project.domain.model.setting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.campusform.server.project.application.dto.request.CreateProjectRequest.RequiredMappings;
import com.campusform.server.project.domain.model.setting.value.ProjectState;
import com.campusform.server.project.domain.model.setting.value.SyncStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프로젝트(모집 공고) Entity
 * Project Context의 핵심 도메인 모델입니다.
 */
@Entity
@Table(name = "projects", indexes = @Index(name = "idx_owner_id", columnList = "owner_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    // 다른 어그리거트 -> 참조 아닌 연관으로 관계 설정
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectState state = ProjectState.DOCUMENT_OPEN;

    @Column(name = "sheet_url", nullable = false)
    private String sheetUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_sync_status", nullable = false)
    private SyncStatus lastSyncStatus = SyncStatus.OK;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    /**
     * 모집 시작일
     */
    @Column(name = "start_at", nullable = false)
    private LocalDate startAt;

    /**
     * 모집 종료일
     */
    @Column(name = "end_at", nullable = false)
    private LocalDate endAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectAdmin> admins = new ArrayList<>();

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProjectRequiredMapping mapping = new ProjectRequiredMapping();

    public static Project create(String title, Long ownerId, String sheetUrl, LocalDate startAt, LocalDate endAt) {
        validate(title, ownerId, sheetUrl, startAt, endAt);
        Project project = new Project();
        project.title = title;
        project.ownerId = ownerId;
        project.sheetUrl = sheetUrl;
        project.startAt = startAt;
        project.endAt = endAt;
        return project;
    }

    /**
     * 연관관계 메서드
     * 
     * Project가 루트 애그리거트이므로 연관관계 설정 후 Project만 저장하면 됩니다.
     */
    public void addAdmin(Long adminId) {
        if (adminId == null)
            throw new IllegalArgumentException("adminId가 필요합니다.");
        if (hasAdmin(adminId))
            throw new IllegalArgumentException("이미 추가된 관리자입니다.");
        admins.add(ProjectAdmin.create(this, adminId));
    }

    public void addMapping(RequiredMappings mappings) {
        this.mapping = ProjectRequiredMapping.create(this, mappings);
    }

    private boolean hasAdmin(Long adminId) {
        return admins.stream().anyMatch(admin -> adminId.equals(admin.getAdminId()));
    }

    private static void validate(String title, Long ownerId, String sheetUrl, LocalDate startAt, LocalDate endAt) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("프로젝트명이 필요합니다.");
        if (ownerId == null)
            throw new IllegalArgumentException("onwerId가 필요합니다.");
        if (sheetUrl == null || sheetUrl.isBlank())
            throw new IllegalArgumentException("sheetUrl가 필요합니다.");
        if (startAt == null || endAt == null)
            throw new IllegalArgumentException("startAt 및 endAt가 필요합니다.");
        if (endAt.isBefore(startAt))
            throw new IllegalArgumentException("startAt이 endAt보다 이후여야 합니다.");
    }
}
