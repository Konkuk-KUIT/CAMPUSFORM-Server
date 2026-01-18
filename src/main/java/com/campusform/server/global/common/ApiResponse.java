package com.campusform.server.global.common;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor // 생성자를 자동으로 만들어줍니다
public class ApiResponse<T> {

    private String status;  // "SUCCESS" or "FAIL"
    private String message; // "조회 성공"
    private T data;         // 실제 데이터 (ResultListResponse 등)

    // 1. 데이터와 메시지를 함께 보내는 성공 메서드 (님 코드가 원하던 것!)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    // 2. 데이터만 보내는 성공 메서드 (나중에 필요할 수 있음)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    // 3. 실패 메서드 (에러 처리용)
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>("FAIL", message, null);
    }
}
