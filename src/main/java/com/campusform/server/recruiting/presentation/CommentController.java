package com.campusform.server.recruiting.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campusform.server.identity.application.service.AuthService;
import com.campusform.server.recruiting.application.dto.request.CommentRequest;
import com.campusform.server.recruiting.application.dto.response.CommentCreateResponse;
import com.campusform.server.recruiting.application.dto.response.CommentResponse;
import com.campusform.server.recruiting.application.dto.response.CommentUpdateResponse;
import com.campusform.server.recruiting.application.service.CommentService;
import com.campusform.server.recruiting.domain.model.applicant.value.RecruitmentStage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}")
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    /**
     * 특정 단계의 지원자에 달린 댓글 조회
     */
    @GetMapping("/applicants/{applicantId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long applicantId,
            @RequestParam RecruitmentStage stage) {
        List<CommentResponse> comments = commentService.getComments(applicantId, stage);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 작성
     * parentId가 있으면 대댓글, 없으면 루트 댓글
     */
    @PostMapping("/applicants/{applicantId}/comments")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable Long applicantId,
            @RequestParam RecruitmentStage stage,
            @RequestBody @Valid CommentRequest requestCommentRequest,
            Authentication authentication) {
        Long memberId = authService.extractUserId(authentication);
        CommentCreateResponse response = commentService.createComment(applicantId, memberId, stage,
                requestCommentRequest);
        return ResponseEntity.ok(response);
    }

    /** 댓글 수정 (작성자 본인만 가능) */
    @PatchMapping("/applicants/{applicantId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam RecruitmentStage stage,
            @RequestBody @Valid CommentRequest request,
            Authentication authentication) {
        Long memberId = authService.extractUserId(authentication);
        CommentUpdateResponse response = commentService.updateComment(
                applicantId, commentId, memberId, stage, request);
        return ResponseEntity.ok(response);
    }

    /** 댓글 삭제 (작성자 본인만 가능) */
    @DeleteMapping("/applicants/{applicantId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @RequestParam RecruitmentStage stage,
            Authentication authentication) {
        Long memberId = authService.extractUserId(authentication);
        commentService.deleteComment(commentId, memberId, stage);
        return ResponseEntity.ok().build();
    }
}
