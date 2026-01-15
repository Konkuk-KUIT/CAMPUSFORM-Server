package com.campusform.server.project.domain.model.setting;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.campusform.server.project.application.dto.request.CreateProjectRequest.RequiredMappings;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_mappings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProjectRequiredMapping {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private Integer nameIdx;
    private Integer schoolIdx;
    private Integer majorIdx;
    private Integer genderIdx;
    private Integer phoneIdx;
    private Integer emailIdx;
    private Integer positionIdx;

    public static ProjectRequiredMapping create(Project project, RequiredMappings requiredMappings) {
        ProjectRequiredMapping mapping = new ProjectRequiredMapping();
        mapping.project = project;
        mapping.nameIdx = requiredMappings.getNameIdx();
        mapping.schoolIdx = requiredMappings.getSchoolIdx();
        mapping.majorIdx = requiredMappings.getMajorIdx();
        mapping.genderIdx = requiredMappings.getGenderIdx();
        mapping.phoneIdx = requiredMappings.getPhoneIdx();
        mapping.emailIdx = requiredMappings.getEmailIdx();
        mapping.positionIdx = requiredMappings.getPositionIdx();
        return mapping;
    }
}
