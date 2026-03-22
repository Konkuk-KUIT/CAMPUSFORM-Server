package com.campusform.server.recruiting.application.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.campusform.server.global.event.UserDeletedEvent;
import com.campusform.server.recruiting.infrastructure.persistence.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecruitingUserEventListener {

    private final CommentRepository commentRepository;

    @EventListener
    @Transactional
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        Long userId = event.userId();
        log.info("UserDeletedEvent 수신: userId={}", userId);

        log.info("사용자가 작성한 댓글 삭제: authorId={}", userId);
        commentRepository.deleteByAuthorId(userId);
    }
}
