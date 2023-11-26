package com.multi.blogging.multiblogging.comment.repository.custom;

import com.multi.blogging.multiblogging.comment.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {
    Optional<Comment> findByIdWithMember(Long id);

    List<Comment> findByBoardIdWithMemberAndReComment(Long boardId);
}
