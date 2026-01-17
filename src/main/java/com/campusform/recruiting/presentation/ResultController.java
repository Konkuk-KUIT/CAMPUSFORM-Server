package com.campusform.recruiting.presentation;


import com.campusform.recruiting.application.ResultService;
import com.campusform.recruiting.application.dto.ResultAnnouncementRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;
    @PostMapping("/announce")
    public ResponseEntity<Void> announceResult(@RequestBody ResultAnnouncementRequest request){
        // 1. 만약 요청 데이터가 이상하면 여기서 컷! (Validation)
        if (request.applicantIds() == null || request.applicantIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 2. 서비스 호출 (방아쇠 당기기)
        resultService.announceResults(request);

        // 3. 클라이언트에게 "성공했어!"라고 HTTP 상태 코드(200)로 응답
        return ResponseEntity.ok().build();

    }
}
