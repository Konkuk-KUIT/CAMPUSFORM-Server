package com.campusform.server.project.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusform.server.project.domain.model.setting.Project;

/**
 * Spring Data JPA를 위한 Project Repository
 * 
 * Spring Data JPA의 JpaRepository를 상속하여
 * 기본 CRUD 메서드와 커스텀 쿼리 메서드를 사용할 수 있습니다.
 * 
 * 도메인 Repository 인터페이스와는 별도로 관리됩니다.
 */
@Repository
public interface JpaProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findBySheetUrl(String sheetUrl);
}
