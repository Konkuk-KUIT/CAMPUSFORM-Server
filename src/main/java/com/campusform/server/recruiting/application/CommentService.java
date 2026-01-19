package com.campusform.server.recruiting.application;

import com.campusform.server.recruiting.application.dto.request.CommentRequest;
import com.campusform.server.recruiting.application.dto.response.CommentCreateResponse;
import com.campusform.server.recruiting.domain.model.comment.Comment;
import com.campusform.server.recruiting.domain.repository.ApplicantRepository;
import com.campusform.server.recruiting.infrastructure.persistence.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ApplicantRepository applicantRepository;
    private final MemberRepository memberRepository;

    // 1. 댓글 작성
    public CommentCreateResponse createComment(Long projectId,Long applicantId,Long memberId, CommentRequest request){
        // 조회 로직

        //
        Comment comment = Comment.create(applicant, member, request.getContent());
        commentRepository.save(comment);

        return new CommentCreateResponse(comment.getId());
    }

    // 2. 댓글 수정
    public CommentDto.UpdateResponse updateComment(Long commentId, Long memberId,CommentDto.Request request){
        Comment comment = commentRepository.findById(commentId);
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 작성자 본인 확인 (기능 명세서 7.3: 본인이 작성한 댓글만 수정 가능)
        // if (!comment.isWrittenBy(memberId)) { throw new AccessDeniedException(...); }

        comment.updateContent(request.content());

        return new CommentDto.UpdateResponse(comment.getId(),comment.getUpdatedAt());

    }

    // 3. 댓글 삭제
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 작성자 본인 확인 로직 필요

        commentRepository.delete(comment);
    }
}
