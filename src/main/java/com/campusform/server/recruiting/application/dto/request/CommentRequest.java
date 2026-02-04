package com.campusform.server.recruiting.application.dto.request;

<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
=======
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
>>>>>>> 4e847b64815eecf19c0c35459dfbf2b688bd9bf3
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
=======
@Schema(description = "댓글 작성/수정 요청")
>>>>>>> 4e847b64815eecf19c0c35459dfbf2b688bd9bf3
@Getter
@NoArgsConstructor // JSON 파싱용
@AllArgsConstructor // 테스트 편하기위함
public class CommentRequest {
<<<<<<< HEAD
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    // parentId가 있으면 대댓글, null이면 루트 댓글
    private Long parentId;
=======
    @Schema(description = "댓글 내용", example = "이 지원자는 꼭 뽑아야 합니다.")
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
>>>>>>> 4e847b64815eecf19c0c35459dfbf2b688bd9bf3
}

