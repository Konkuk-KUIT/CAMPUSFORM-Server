package com.campusform.server.recruiting.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.campusform.server.recruiting.domain.model.interview.schedule.InterviewScheduledSlot;

@Repository
public interface InterviewScheduledSlotJpaRepository extends JpaRepository<InterviewScheduledSlot, Long> {

    List<InterviewScheduledSlot> findByProjectId(Long projectId);

    @Modifying
    @Query("DELETE FROM InterviewScheduledSlot s WHERE s.projectId = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
