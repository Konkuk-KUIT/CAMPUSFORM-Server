package com.campusform.server.project.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.campusform.server.project.application.dto.ColumnInfo;
import com.campusform.server.project.domain.service.SpreadsheetReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpreadsheetService {

    private final SpreadsheetReader spreadsheetReader;

    public List<ColumnInfo> getHeaders(String sheetUrl) {
        return spreadsheetReader.readHeader(sheetUrl);
    }
}