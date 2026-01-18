package com.campusform.server.recruiting.domain.repository;

import com.campusform.server.recruiting.domain.model.message.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate,Long> {

}
