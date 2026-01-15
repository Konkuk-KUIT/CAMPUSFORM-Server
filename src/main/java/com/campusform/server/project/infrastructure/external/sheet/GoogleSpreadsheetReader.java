package com.campusform.server.project.infrastructure.external.sheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.campusform.server.project.application.dto.ColumnInfo;
import com.campusform.server.project.domain.service.SpreadsheetReader;

/**
 * Google 스프레드시트 읽기 구현체
 * 
 * DDD 관점에서 외부 시스템 연동은 infrastructure 계층에 위치합니다.
 * 
 * 실제 구현 시:
 * 1. Google Sheets API 클라이언트를 주입받아 사용
 * 2. 스프레드시트 URL을 파싱하여 스프레드시트 ID 추출
 * 3. Google Sheets API를 통해 첫 번째 행의 데이터를 읽어옴
 * 
 * 현재는 Google OAuth가 구현되지 않아 비활성화되어 있습니다.
 * OAuth 구현 후 @Component를 활성화하고 CsvSpreadsheetReader의 @Primary를 제거하면 됩니다.
 */
// @Component // Google OAuth 구현 전까지 비활성화
public class GoogleSpreadsheetReader implements SpreadsheetReader {

    /**
     * 스프레드시트의 첫 번째 행(헤더)을 읽어서 컬럼명과 인덱스를 반환합니다.
     * 
     * @param sheetUrl 스프레드시트 URL
     * @return 컬럼 정보 목록 (컬럼명과 인덱스 포함)
     */
    @Override
    public List<ColumnInfo> readHeader(String sheetUrl) {
        // TODO: 실제 Google Sheets API 연동 구현
        // 예시: 스프레드시트의 첫 번째 행에서 컬럼명들을 가져온다고 가정
        // 실제로는 Google Sheets API를 통해 데이터를 읽어와야 합니다.

        // 세세한 연동 과정 생략 - 예시 데이터 반환
        // 실제 구현 시에는 스프레드시트 URL을 파싱하여 스프레드시트 ID를 추출하고,
        // Google Sheets API를 통해 첫 번째 행의 데이터를 읽어와야 합니다.

        // 예시: 스프레드시트의 실제 헤더 순서 (프로젝트마다 다를 수 있음)
        // 이 예시에서는 [이름, 학교, 학과, 성별, 전화번호, 이메일, 지원 포지션, ...] 순서
        List<String> exampleColumnNames = Arrays.asList(
                "이름을 작성해주세요.", // index 0
                "학교를 작성해주세요.", // index 1
                "학과를 작성해주세요.", // index 2
                "성별을 선택해주세요.", // index 3
                "전화번호를 입력해주세요.", // index 4
                "이메일 주소", // index 5
                "지원 포지션을 선택해주세요.", // index 6
                "나이를 작성해주세요.", // index 7
                "간단하게 자기소개를 해주세요.", // index 8
                "지원 동기를 써주세요." // index 9
        );

        // 컬럼명과 인덱스를 함께 포함한 도메인 모델 리스트 생성
        // 인덱스는 스프레드시트에서의 실제 컬럼 순서를 나타냅니다.
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (int i = 0; i < exampleColumnNames.size(); i++) {
            columnInfos.add(new ColumnInfo(
                    exampleColumnNames.get(i),
                    i // 인덱스는 0부터 시작 (스프레드시트의 실제 컬럼 순서)
            ));
        }

        return columnInfos;
    }
}
