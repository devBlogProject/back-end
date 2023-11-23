package com.multi.blogging.multiblogging.comment.repository.custom;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.comment.domain.ReComment;

import java.util.Optional;

public interface CustomReCommentRepository {

    Optional<ReComment> findByIdWithMember(Long id);
}
