package com.campusform.server.project.infrastructure.external.sheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.campusform.server.project.application.dto.ColumnInfo;
import com.campusform.server.project.domain.service.SpreadsheetReader;

/**
 * CSV 파일을 읽어오는 스프레드시트 리더 구현체
 * 
 * Google OAuth가 구현되기 전까지 임시로 사용하는 구현체입니다.
 * CSV 파일의 첫 번째 행(헤더)을 읽어서 컬럼명과 인덱스를 반환합니다.
 * 
 * @Primary: Google OAuth가 구현되기 전까지 기본 구현체로 사용됩니다.
 */
@Component
@Primary
public class CsvSpreadsheetReader implements SpreadsheetReader {

    @Override
    public List<ColumnInfo> readHeader(String sheetUrl) {
        String csvUrl = getCsvUrl(sheetUrl);

        List<ColumnInfo> columnInfos = new ArrayList<>();

        try {
            URL url = new URL(csvUrl);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {

                String header = reader.readLine();
                if (header == null || header.trim().isEmpty()) {
                    throw new IllegalArgumentException("CSV 파일의 헤더가 비어있습니다.");
                }

                String[] columns = header.split(",");
                // CSV 헤더를 순회하며 컬럼명과 인덱스를 도메인 모델로 변환
                for (int i = 0; i < columns.length; i++) {
                    String columnName = columns[i].trim();
                    // 빈 컬럼은 제외
                    if (!columnName.isEmpty()) {
                        columnInfos.add(new ColumnInfo(columnName, i));
                    }
                }
            }
            return columnInfos;

        } catch (IOException e) {
            throw new RuntimeException("CSV 파일을 읽는 중 오류가 발생했습니다: " + sheetUrl, e);
        }
    }

    @Override
    public List<String> readAllLines(String sheetUrl) {
        String csvUrl = getCsvUrl(sheetUrl);

        List<String> lines = new ArrayList<>();

        List<ColumnInfo> columnInfos = new ArrayList<>();

        try {
            URL url = new URL(csvUrl);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {

                String header = reader.readLine();
                if (header == null || header.trim().isEmpty()) {
                    throw new IllegalArgumentException("CSV 파일의 헤더가 비어있습니다.");
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            return lines;

        } catch (IOException e) {
            throw new RuntimeException("CSV 파일을 읽는 중 오류가 발생했습니다: " + sheetUrl, e);
        }
    }

    public static String getCsvUrl(String sheetUrl) {
        String id = sheetUrl.split("/d/")[1].split("/")[0];
        return "https://docs.google.com/spreadsheets/d/" + id + "/export?format=csv";
    }
}
