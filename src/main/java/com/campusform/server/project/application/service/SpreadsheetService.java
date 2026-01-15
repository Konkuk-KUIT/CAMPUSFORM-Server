package com.campusform.server.project.application.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.campusform.server.project.application.dto.ColumnInfo;
import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.project.domain.model.setting.ProjectRequiredMapping;
import com.campusform.server.project.domain.repository.ProjectRepository;
import com.campusform.server.project.domain.service.SpreadsheetReader;
import com.campusform.server.recruiting.domain.model.applicant.Applicant;
import com.campusform.server.recruiting.domain.model.applicant.repository.ApplicantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpreadsheetService {

    /**
     * 외부 연동과 관련있기에 도메인 서비스 사용
     */
    private final SpreadsheetReader spreadsheetReader;

    private final ProjectRepository projectRepository;
    private final ApplicantRepository applicantRepository;

    public List<ColumnInfo> getHeaders(String sheetUrl) {
        return spreadsheetReader.readHeader(sheetUrl);
    }

    /**
     * 최소 동기화 메서드
     */
    public void syncInit(String sheetUrl) {
        Project project = projectRepository.findBySheetUrl(sheetUrl)
                .orElseThrow(() -> new IllegalArgumentException("sheetUrl에 해당하는 프로젝트가 존재하지 않습니다."));

        Long projectId = project.getId();

        /**
         * 필수 매핑 항목
         */
        ProjectRequiredMapping mappings = project.getMapping();
        Set<Integer> requiredMapping = new HashSet<>();
        requiredMapping.add(mappings.getNameIdx());
        requiredMapping.add(mappings.getEmailIdx());
        requiredMapping.add(mappings.getPhoneIdx());
        requiredMapping.add(mappings.getGenderIdx());
        requiredMapping.add(mappings.getSchoolIdx());
        requiredMapping.add(mappings.getMajorIdx());
        requiredMapping.add(mappings.getPositionIdx());

        List<ColumnInfo> header = spreadsheetReader.readHeader(sheetUrl);
        int maxIdx = header.stream().map(ColumnInfo::getIndex).max(Integer::compareTo).orElse(0);

        List<String> lines = spreadsheetReader.readAllLines(sheetUrl);

        String name = null, email = null, phone = null, gender = null, school = null, major = null, position = null;

        for (String line : lines) {
            String[] columns = line.split(",");

            Map<String, String> unmappingMap = new HashMap<>();
            for (int i = 0; i <= maxIdx; i++) {
                if (requiredMapping.contains(i)) {
                    if (i == mappings.getNameIdx())
                        name = columns[i];
                    if (i == mappings.getEmailIdx())
                        email = columns[i];
                    if (i == mappings.getPhoneIdx())
                        phone = columns[i];
                    if (i == mappings.getGenderIdx())
                        gender = columns[i];
                    if (i == mappings.getSchoolIdx())
                        school = columns[i];
                    if (i == mappings.getMajorIdx())
                        major = columns[i];
                    if (i == mappings.getPositionIdx())
                        position = columns[i];
                } else {
                    unmappingMap.put(header.get(i).getName(), columns[i]);
                }
            }

            Applicant applicant = Applicant.create(projectId, name, email, phone, gender, school, major, position);
            Iterator<String> keys = unmappingMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                applicant.addExtraAnswer(key, unmappingMap.get(key));
            }
            applicantRepository.save(applicant);
        }
    }
}
