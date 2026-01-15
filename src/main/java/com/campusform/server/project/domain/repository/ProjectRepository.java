package com.campusform.server.project.domain.repository;

import java.util.Optional;

import com.campusform.server.project.domain.model.setting.Project;

/**
 * Project Repository 인터페이스 (도메인 관점)
 * 
 * DDD 관점에서 Repository는 도메인 계층에 인터페이스로 정의됩니다.
 * 구현체는 infrastructure 계층에 위치하며, JPA 등 외부 기술을 사용합니다.
 * 
 * 도메인 계층에서는 Spring 어노테이션을 사용하지 않습니다.
 */
public interface ProjectRepository {

    /**
     * 프로젝트 저장
     * 
     * @param project 저장할 프로젝트
     * @return 저장된 프로젝트
     */
    Project save(Project project);

    /**
     * ID로 프로젝트 조회
     * 
     * @param id 프로젝트 ID
     * @return 프로젝트 (없을 수 있음)
     */
    Optional<Project> findById(Long id);
}
