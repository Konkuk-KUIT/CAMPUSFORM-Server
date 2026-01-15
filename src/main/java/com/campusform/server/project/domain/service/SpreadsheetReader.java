package com.campusform.server.project.domain.service;

import java.util.List;

import com.campusform.server.project.application.dto.ColumnInfo;

/**
 * 스프레드시트 읽기 도메인 서비스 인터페이스
 * 
 * DDD 관점에서 외부 시스템과의 연동은
 * 도메인 서비스 인터페이스로 정의하고, 구현체는 infrastructure에 위치합니다.
 * 
 * 도메인 계층에서는 외부 기술에 의존하지 않고 인터페이스만 정의합니다.
 */
public interface SpreadsheetReader {

    /**
     * 스프레드시트의 헤더를 읽어서 컬럼명과 인덱스를 반환합니다.,
     * 
     * @return (칼럼명 - 인덱스) 리스트
     */
    List<ColumnInfo> readHeader(String sheetUrl);
}