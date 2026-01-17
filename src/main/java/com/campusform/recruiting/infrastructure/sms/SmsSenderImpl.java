package com.campusform.recruiting.infrastructure.sms;

import com.campusform.recruiting.application.port.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsSenderImpl implements SmsSender {
    @Override
    public void sendSms(String phoneNumber, String content) {
        // 여기가 진짜 배달하는 곳입니다.
        // 지금은 로그만 찍지만, 나중에는 CoolSMS, Twilio 같은 외부 API 코드가 들어갑니다.

        // 예: 실제 외부 API 연동 코드
        // messageService.sendOne(new SingleMessage(phoneNumber, content));

        log.info("[SMS 전송] 수신번호: {}, 내용: {}", phoneNumber, content);
    }
//    @Override
//    public void sendPassNotification(String phoneNumber, String applicantName) {
//        log.info("[SMS 발송] To: {}, 내용: {}님, 축하합니다! 서류 전형에 합격하셨습니다.", phoneNumber, applicantName);
//    }
//    @Override
//    public void sendFailNotification(String phoneNumber, String applicantName) {
//        log.info("[SMS 발송] To: {}, 내용: {}님, 아쉽게도 이번에는 함께하지 못하게 되었습니다.", phoneNumber, applicantName);
//    }
}
