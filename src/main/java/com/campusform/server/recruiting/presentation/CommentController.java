package com.campusform.server.recruiting.presentation;

import com.campusform.server.recruiting.application.service.CommentService;
import com.campusform.server.recruiting.application.dto.request.CommentRequest;
import com.campusform.server.recruiting.application.dto.response.CommentCreateResponse;
import com.campusform.server.recruiting.application.dto.response.CommentUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/applicants/{applicantId}/comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(
        @PathVariable Long projectId,
        @PathVariable Long applicantId,
        @RequestParam(defaultValue = "document") String stage,
        @RequestBody CommentRequest request
    ){
        Long memberId = 1L; // 임시 하드코딩 (로그인 구현 시 교체 필요)

        CommentCreateResponse response = commentService.createComment(applicantId, memberId, request);

        return ResponseEntity.ok(response);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "document") String stage,
            @RequestBody CommentRequest request
    ) {
        Long memberId = 1L; // 임시 하드코딩

        CommentUpdateResponse response = commentService.updateComment(commentId, memberId, request);

        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long projectId,
            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "document") String stage
    ) {
        Long memberId = 1L; // 임시 하드코딩

        commentService.deleteComment(commentId, memberId);

        return ResponseEntity.ok().build();
    }
}
