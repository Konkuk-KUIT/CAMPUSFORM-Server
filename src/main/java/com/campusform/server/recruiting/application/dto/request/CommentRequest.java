package com.campusform.server.recruiting.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 파싱용
@AllArgsConstructor // 테스트 편하기위함
public class CommentRequest {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    // parentId가 있으면 대댓글, null이면 루트 댓글
    private Long parentId;
}

