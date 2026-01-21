package com.campusform.server.recruiting.domain.model.message;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * 프로젝트 메시지 템플릿 Entity
 * 프로젝트별 문자 템플릿을 관리합니다.
 * 프로젝트 템플릿은 부가정보로 별도 테이블로 관리됩니다.
 */
@Entity
@Table(name = "project_message_templates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MessageTemplate{
    @Id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /**
     * 서류 합격 메시지 템플릿
     */
    @Column(name = "template_document_pass", columnDefinition = "TEXT")
    private String templateDocumentPass;

    /**
     * 서류 불합격 메시지 템플릿
     */
    @Column(name = "template_document_fail", columnDefinition = "TEXT")
    private String templateDocumentFail;

    /**
     * 면접 합격 메시지 템플릿
     */
    @Column(name = "template_interview_pass", columnDefinition = "TEXT")
    private String templateInterviewPass;

    /**
     * 면접 불합격 메시지 템플릿
     */
    @Column(name = "template_interview_fail", columnDefinition = "TEXT")
    private String templateInterviewFail;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // MessageTemplate.java 내부에 추가
    public static MessageTemplate createEmpty(Long projectId) {
        MessageTemplate template = new MessageTemplate();
        template.projectId = projectId;
        return template;
    }

    public void updateTemplate(String stage, String status, String content) {
        if (!"DOCUMENT".equalsIgnoreCase(stage) && !"INTERVIEW".equalsIgnoreCase(stage)) {
            throw new IllegalArgumentException("stage must be DOCUMENT or INTERVIEW");
        }
        if (!"PASS".equalsIgnoreCase(status) && !"FAIL".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("status must be PASS or FAIL");
        }
        if ("DOCUMENT".equalsIgnoreCase(stage)) {
            if ("PASS".equalsIgnoreCase(status)) this.templateDocumentPass = content;
            else this.templateDocumentFail = content;
        } else { // INTERVIEW
            if ("PASS".equalsIgnoreCase(status)) this.templateInterviewPass = content;
            else this.templateInterviewFail = content;
        }
    }
}
