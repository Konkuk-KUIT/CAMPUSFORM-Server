package com.campusform.server.recruiting.application.eventhandler;

import com.campusform.server.recruiting.application.port.SmsSender;
import com.campusform.recruiting.domain.applicant.EvaluationStatus;
import com.campusform.server.recruiting.domain.model.event.ApplicantUpdated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j // 2. 추가됨 (log 사용 가능)
@Component // 3. 추가됨 (스프링 빈 등록)
@RequiredArgsConstructor
public class ApplicantEventHandler {

    private final SmsSender smsSender;

    // 4. 문구 템플릿 정의 (상수로 관리)
    private static final String PASS_TEMPLATE = "[CAMPUS:FORM] %s님, 축하합니다! 서류 전형에 합격하셨습니다.";
    private static final String FAIL_TEMPLATE = "[CAMPUS:FORM] %s님, 아쉽게도 이번 전형에서 모시지 못하게 되었습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApplicantUpdated(ApplicantUpdated event) {
        log.info("이벤트 수신: 지원자 ID={}, 상태={}", event.applicantId(), event.status());

        // 5. 상태에 따라 다른 문구 생성
        String message = null;

        if (event.status() == EvaluationStatus.PASSED) {
            // 이름(%s) 치환해서 문구 완성
            message = String.format(PASS_TEMPLATE, event.applicantName());
        } else if (event.status() == EvaluationStatus.FAILED) {
            message = String.format(FAIL_TEMPLATE, event.applicantName());
        }

        // 6. 보낼 메시지가 있을 때만(합/불 일때만) 전송
        if (message != null) {
            smsSender.sendSms(event.applicantPhone(), message);
            log.info("문자 발송 완료: To={}, Msg={}", event.applicantPhone(), message);
        }
    }
}
