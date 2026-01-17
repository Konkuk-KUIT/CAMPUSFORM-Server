package com.campusform.recruiting.presentation;


import com.campusform.recruiting.application.ResultService;
import com.campusform.recruiting.application.dto.ResultAnnouncementRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public class ResultController {
    private final ResultService resultService;
    @PostMapping("/announce")
    public ResponseEntity<Void> announceResult(@RequestBody ResultAnnouncementRequest request){
        resultService.announceResults(request);
        return ResponseEntity.ok().build();
    }
}
