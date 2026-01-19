package com.campusform.server.recruiting.presentation;

import com.campusform.server.global.common.ApiResponse;
import com.campusform.server.recruiting.application.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/applicants/{applicantId}/comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ApiResponse<CommentDto.CreateResponse> createComment(
        @PathVariable Long projectId,
        @PathVariable Long applicantId,
        @RequestParam(defaultValue = "document") String stage,
        @RequestBody CommentDto.Request request
    ){
        Long memberId = 1L; // 임시 하드코딩 (로그인 구현 시 교체 필요)

        CommentDto.CreateResponse response = commentService.createComment(projectId, applicantId, memberId, request);

        return ApiResponse.success("댓글 작성에 성공했습니다.", response);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ApiResponse<CommentDto.UpdateResponse> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "document") String stage,
            @RequestBody CommentDto.Request request
    ) {
        Long memberId = 1L; // 임시 하드코딩

        CommentDto.UpdateResponse response = commentService.updateComment(commentId, memberId, request);

        return ApiResponse.success("댓글 수정에 성공했습니다.", response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long projectId,
            @PathVariable Long applicantId,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "document") String stage
    ) {
        Long memberId = 1L; // 임시 하드코딩

        commentService.deleteComment(commentId, memberId);

        return ApiResponse.success("댓글 삭제에 성공했습니다.");
    }
}
