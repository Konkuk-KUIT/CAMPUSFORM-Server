package com.campusform.recruiting.domain.applicant;


import com.campusform.recruiting.domain.event.ApplicantUpdated;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="applicants",uniqueConstraints = {
        @UniqueConstraint(name="uk_project_name_email",columnNames = {"project_id","name","email"})
})
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
// 변경점 1: AbstractAggregateRoot 상속 (이벤트 발행 기능 추가)
public class Applicant extends AbstractAggregateRoot<Applicant> {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="project_id",nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private String name;
    private String school;
    private String major;
    private String gender;
    private String phone;
    @Column(nullable = false)
    private String email;
    private String position;

    /**
     * 서류 단계 심사 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name="document_status",nullable = false)
    private EvaluationStatus documentStatus = EvaluationStatus.PENDING;

    @Column(nullable = false)
    private Boolean bookmarked = false;

    @OneToMany(mappedBy = "applicant",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicantExtraAnswer> extraAnswers = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Applicant create(Long projectId, String name, String email, String phone, String gender,
                                   String school, String major, String position) {
        Applicant applicant = new Applicant();
        applicant.projectId = projectId;
        applicant.name = name;
        applicant.email = email;
        applicant.phone = phone;
        applicant.gender = gender;
        applicant.school = school;
        applicant.major = major;
        applicant.position = position;
        return applicant;
    }

    public void addExtraAnswer(String questionText, String answerText) {
        extraAnswers.add(ApplicantExtraAnswer.create(this, questionText, answerText));
    }
    /**
     * [비즈니스 로직] 서류 심사 결과 업데이트 및 이벤트 발행
     */
    public void updateEvaluationStatus(EvaluationStatus newStatus) {
        if(this.documentStatus == newStatus) {
            return ;
        }
        this.documentStatus = newStatus;

        this.registerEvent(new ApplicantUpdated(
                this.id,
                this.name,
                this.phone,
                this.documentStatus
        ));
    }

}
