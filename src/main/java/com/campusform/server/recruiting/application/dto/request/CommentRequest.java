package com.campusform.server.recruiting.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 파싱용
@AllArgsConstructor // 테스트 편하기위함
public class CommentRequest {
    private String content;
}

