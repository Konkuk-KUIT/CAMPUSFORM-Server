package com.campusform.server.global.exception;

/**
 * 인증이 없거나(로그인 필요), 인증 컨텍스트가 유효하지 않을 때 사용하는 예외입니다.
 *
 * - 컨트롤러/서비스에서 "401 Unauthorized"로 응답해야 하는 상황을 RuntimeException으로 표현합니다.
 * - 실제 HTTP 매핑은 {@link GlobalExceptionHandler}에서 처리합니다.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
