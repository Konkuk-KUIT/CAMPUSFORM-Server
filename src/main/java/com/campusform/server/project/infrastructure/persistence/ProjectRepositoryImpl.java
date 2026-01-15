package com.campusform.server.project.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.project.domain.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

/**
 * ProjectRepository 구현체
 * 
 * 적절히 SpringDataJpa 또는 Querydsl에 작업을 위임합니다.
 */
@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final JpaProjectRepository jpaProjectRepository;

    @Override
    public Project save(Project project) {
        return jpaProjectRepository.save(project);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return jpaProjectRepository.findById(id);
    }

    @Override
    public Optional<Project> findBySheetUrl(String sheetUrl) {
        return jpaProjectRepository.findBySheetUrl(sheetUrl);
    }
}
