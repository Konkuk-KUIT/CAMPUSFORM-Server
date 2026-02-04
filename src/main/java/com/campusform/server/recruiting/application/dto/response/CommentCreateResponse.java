package com.campusform.server.recruiting.application.dto.response;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateResponse {
    private Long commentId;
    private Long parentId;
=======
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "댓글 작성 응답")
@Getter
@AllArgsConstructor
public class CommentCreateResponse {
    @Schema(description = "생성된 댓글 ID", example = "101")
    private Long commentId;
>>>>>>> 4e847b64815eecf19c0c35459dfbf2b688bd9bf3
}
