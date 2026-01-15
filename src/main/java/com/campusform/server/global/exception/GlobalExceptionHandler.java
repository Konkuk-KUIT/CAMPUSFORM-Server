package com.campusform.server.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 전역 예외 처리 핸들러
 * 
 * @RestControllerAdvice를 사용하여 모든 컨트롤러에서 발생하는 예외를
 *                        한 곳에서 처리합니다. 이를 통해:
 *                        1. 일관된 에러 응답 형식 제공
 *                        2. 예외 처리 로직 중복 제거
 *                        3. 예외 발생 시 적절한 HTTP 상태 코드 반환
 * 
 *                        실무에서는 더 세밀한 예외 분류와 에러 코드 체계를 구축합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 요청 데이터 검증 실패 예외 처리
     * 
     * @Valid 어노테이션으로 검증 실패 시 발생하는 예외를 처리합니다.
     *        각 필드의 검증 오류 메시지를 모아서 반환합니다.
     * 
     * @param ex MethodArgumentNotValidException
     * @return 에러 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 각 필드의 검증 오류를 맵에 저장
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                "입력 데이터 검증에 실패했습니다.",
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 잘못된 인자 예외 처리
     * 
     * 비즈니스 로직에서 발생하는 IllegalArgumentException을 처리합니다.
     * 예: 존재하지 않는 프로젝트 조회, 중복된 관리자 추가 등
     * 
     * @param ex IllegalArgumentException
     * @return 에러 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse(
                "ILLEGAL_ARGUMENT",
                ex.getMessage(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 잘못된 상태 예외 처리
     * 
     * 비즈니스 로직에서 발생하는 IllegalStateException을 처리합니다.
     * 예: 이미 추가된 관리자, 프로젝트 상태 변경 불가 등
     * 
     * @param ex IllegalStateException
     * @return 에러 응답
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponse response = new ErrorResponse(
                "ILLEGAL_STATE",
                ex.getMessage(),
                null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 기타 예외 처리
     * 
     * 위에서 처리하지 않은 모든 예외를 처리합니다.
     * 실무에서는 로깅을 추가하고, 상세한 에러 정보는 클라이언트에 노출하지 않습니다.
     * 
     * @param ex Exception
     * @return 에러 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        ex.printStackTrace();
        // 실무에서는 로깅 추가
        // log.error("Unexpected error occurred", ex);

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다." + ex.getMessage(),
                null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 에러 응답 DTO
     * 클라이언트에게 반환할 에러 정보를 담는 객체입니다.
     */
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        /**
         * 에러 코드 (클라이언트에서 에러 타입을 구분하기 위해 사용)
         */
        private String code;

        /**
         * 에러 메시지 (사용자에게 보여줄 메시지)
         */
        private String message;

        /**
         * 상세 에러 정보 (검증 오류 등에서 사용)
         */
        private Map<String, String> details;
    }
}
