package com.campusform.server.recruiting.domain.repository;

import java.util.List;

import com.campusform.server.recruiting.domain.model.interview.schedule.InterviewScheduledSlot;

/**
 * 배정된 면접 슬롯 애그리거트 Repository
 */
public interface InterviewScheduledSlotRepository {

    InterviewScheduledSlot save(InterviewScheduledSlot slot);

    void saveAll(List<InterviewScheduledSlot> slots);

    List<InterviewScheduledSlot> findByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);
}
