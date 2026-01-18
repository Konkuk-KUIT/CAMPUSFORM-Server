package com.campusform.server.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 비동기 처리 설정
 *
 * @EnableAsync를 통해 @Async 어노테이션을 활성화합니다.
 * 이벤트 핸들러에서 비동기로 알림을 생성할 수 있도록 합니다.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 기본 SimpleAsyncTaskExecutor 사용
    // 필요시 ThreadPoolTaskExecutor를 빈으로 등록하여 스레드 풀 커스터마이징 가능
}
