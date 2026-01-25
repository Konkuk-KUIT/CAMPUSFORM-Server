package com.campusform.server.recruiting.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.recruiting.application.dto.request.SetRequiredInterviewerRequest;
import com.campusform.server.recruiting.application.dto.request.UpdateRequiredInterviewersRequest;
import com.campusform.server.recruiting.application.dto.response.RequiredInterviewersResponse;
import com.campusform.server.recruiting.application.service.InterviewContextLoader.InterviewContext;
import com.campusform.server.recruiting.domain.model.interview.setup.InterviewSetting;

import lombok.RequiredArgsConstructor;

/**
 * 필수 면접관 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequiredInterviewerService {

    private final InterviewContextLoader contextLoader;

    /**
     * 필수 면접관 목록 조회
     */
    public RequiredInterviewersResponse getRequiredInterviewers(Long projectId, Long userId) {
        InterviewContext ctx = contextLoader.loadContext(projectId);
        ctx.project().validateOwnerAccess(userId);
        InterviewSetting setting = ctx.setting();

        List<Long> adminIds = setting.getRequiredInterviewerIds();
        return RequiredInterviewersResponse.of(adminIds);
    }

    /**
     * 필수 면접관 전체 교체
     */
    @Transactional
    public RequiredInterviewersResponse replaceAllRequiredInterviewers(
            Long projectId, Long userId, UpdateRequiredInterviewersRequest request) {
        InterviewContext ctx = contextLoader.loadContext(projectId);
        Project project = ctx.project();
        project.validateOwnerAccess(userId);
        InterviewSetting setting = ctx.setting();

        // 요청에 포함된 면접관 ID가 프로젝트 관리자인지 검증
        if (request.getAdminIds() != null) {
            for (Long adminId : request.getAdminIds()) {
                project.validateAdminAccess(adminId);
            }
        }

        // 변경 감지(Dirty Checking)
        setting.replaceRequiredInterviewers(request.getAdminIds());

        return RequiredInterviewersResponse.of(setting.getRequiredInterviewerIds());
    }

    /**
     * 필수 면접관 개별 상태 변경 (추가/제거)
     */
    @Transactional
    public RequiredInterviewersResponse updateRequiredInterviewerStatus(
            Long projectId, Long userId, Long adminId, SetRequiredInterviewerRequest request) {
        InterviewContext ctx = contextLoader.loadContext(projectId);
        Project project = ctx.project();
        project.validateOwnerAccess(userId);
        project.validateAdminAccess(adminId);
        InterviewSetting setting = ctx.setting();

        // 영속화된 엔티티이므로 변경 감지(Dirty Checking)로 자동 업데이트됨
        setting.setRequiredInterviewer(adminId, request.getRequired());

        return RequiredInterviewersResponse.of(setting.getRequiredInterviewerIds());
    }
}
