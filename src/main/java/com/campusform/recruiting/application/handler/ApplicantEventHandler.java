package com.campusform.recruiting.application.handler;

import com.campusform.recruiting.domain.applicant.EvaluationStatus;
import com.campusform.recruiting.domain.event.ApplicantUpdated;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public class ApplicantEventHandler {

    private final SmsSender smsSender;

    /**
     * 트랜잭션이 커밋된 후에(AFTER_COMMIT) 실행됩니다.
     * @Async를 붙여 별도 스레드에서 실행하면 메인 로직의 응답 속도에 영향을 주지 않습니다.
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApplicantUpdated(ApplicantUpdated event) {
        log.info("이벤트 수신: 지원자 ID={}, 상태={}", event.applicantId(), event.status());

        // 합격/불합격 상태에 따라서만 문자 발송
        if (event.status() == EvaluationStatus.PASSED) {
            smsSender.sendPassNotification(event.applicantPhone(), event.applicantName());
        } else if (event.status() == EvaluationStatus.FAILED) {
            smsSender.sendFailNotification(event.applicantPhone(), event.applicantName());
        }

        // PENDING으로 돌아간 경우 등에는 문자를 안 보낼 수도 있음
    }
}
