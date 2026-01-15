package com.campusform.server.project.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campusform.server.project.application.dto.ColumnInfo;
import com.campusform.server.project.application.service.SpreadsheetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/spreadsheets")
@RequiredArgsConstructor
public class SpreadSheetController {

    private final SpreadsheetService spreadsheetService;

    /**
     * 스프레드 헤더 조회 for 1대1 드롭다운 매핑
     * 
     * @return 컬럼명-인덱스 리스트
     */
    @GetMapping("/headers")
    public ResponseEntity<List<ColumnInfo>> getSpreadsheetHeaders(@RequestParam String sheetUrl) {
        List<ColumnInfo> response = spreadsheetService.getHeaders(sheetUrl);
        return ResponseEntity.ok(response);
    }

}
