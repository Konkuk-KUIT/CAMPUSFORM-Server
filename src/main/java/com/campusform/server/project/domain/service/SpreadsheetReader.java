package com.campusform.server.project.domain.service;

import java.util.List;

import com.campusform.server.project.application.dto.ColumnInfo;

/**
 * 스프레드시트 읽기 도메인 서비스 인터페이스
 * 구현체는 infrastructure에 위치합니다.
 */
public interface SpreadsheetReader {

    List<ColumnInfo> readHeader(String sheetUrl);

    List<String> readAllLines(String sheetUrl);
}
