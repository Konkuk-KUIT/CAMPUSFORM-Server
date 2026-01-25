package com.campusform.server.recruiting.domain.model.interview.setup;

<<<<<<< HEAD
import java.time.LocalDateTime;

=======
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
>>>>>>> be4f0598136463b0d746738ed0061ae55205e15f
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

=======
import java.time.LocalDateTime;  
         
>>>>>>> be4f0598136463b0d746738ed0061ae55205e15f
/**
 * 필수 면접관 Entity
 * 필수 면접관 관리를 담당합니다.
 */
@Entity
<<<<<<< HEAD
@Table(name = "interview_required_interviewers", uniqueConstraints = {
        @UniqueConstraint(name = "uk_setting_admin", columnNames = { "interview_setting_id", "admin_id" })
})
// indexes = @Index(name = "idx_setting_id", columnList =
// "interview_setting_id"))
=======
@Table(name = "interview_required_interviewers",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_setting_admin", columnNames = {"interview_setting_id", "admin_id"})
       })
       // indexes = @Index(name = "idx_setting_id", columnList = "interview_setting_id"))
>>>>>>> be4f0598136463b0d746738ed0061ae55205e15f
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class InterviewRequiredInterviewer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_setting_id", nullable = false)
    private InterviewSetting setting;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

<<<<<<< HEAD
=======
    @Column(nullable = false)
    private Boolean required = false;

>>>>>>> be4f0598136463b0d746738ed0061ae55205e15f
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
<<<<<<< HEAD

    /**
     * 필수 면접관 생성 팩토리 메서드
     */
    public static InterviewRequiredInterviewer create(InterviewSetting setting, Long adminId) {
        InterviewRequiredInterviewer requiredInterviewer = new InterviewRequiredInterviewer();
        requiredInterviewer.setting = setting;
        requiredInterviewer.adminId = adminId;
        return requiredInterviewer;
    }
=======
>>>>>>> be4f0598136463b0d746738ed0061ae55205e15f
}
