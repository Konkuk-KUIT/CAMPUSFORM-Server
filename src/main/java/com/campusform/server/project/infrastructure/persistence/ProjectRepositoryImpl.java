package com.campusform.server.project.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.campusform.server.project.domain.model.setting.Project;
import com.campusform.server.project.domain.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

/**
 * Project Repository 구현체
 * 
 * DDD 관점에서 Repository 인터페이스는 domain에 있고,
 * 구현체는 infrastructure에 위치합니다.
 * 
 * 이 클래스는 도메인 Repository 인터페이스를 구현하며,
 * 내부적으로 Spring Data JPA Repository를 사용합니다.
 * 
 * Spring Data JPA의 메서드를 직접 사용하려면 JpaProjectRepository를 주입받아 사용하세요.
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
}
