package com.campusform.server.recruiting.presentation;

import com.campusform.server.identity.application.service.AuthService;
import com.campusform.server.recruiting.application.service.CommentService;
import com.campusform.server.recruiting.application.dto.request.CommentRequest;
import com.campusform.server.recruiting.application.dto.response.CommentCreateResponse;
import com.campusform.server.recruiting.application.dto.response.CommentUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/applicants/{applicantId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(
        @PathVariable Long projectId,
        @PathVariable Long applicantId,
        @RequestParam String stage,
        @RequestBody @Valid CommentRequest request,
        @AuthenticationPrincipal Authentication authentication
    ){
        Long memberId = authService.extractUserId(authentication);
        CommentCreateResponse response = commentService.createComment(applicantId, memberId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam String stage,
            @RequestBody @Valid CommentRequest request,
            @AuthenticationPrincipal Authentication authentication
    ) {
        Long memberId = authService.extractUserId(authentication);

        CommentUpdateResponse response = commentService.updateComment(
                projectId, applicantId,commentId, memberId, request
        );

        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
//            @PathVariable Long projectId,
//            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam String stage,
            @AuthenticationPrincipal Authentication authentication
    ) {
        Long memberId = authService.extractUserId(authentication);

        commentService.deleteComment(commentId, memberId);

        return ResponseEntity.ok().build();
    }
}
