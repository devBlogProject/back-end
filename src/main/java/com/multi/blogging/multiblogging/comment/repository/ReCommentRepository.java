package com.multi.blogging.multiblogging.comment.repository;

import com.multi.blogging.multiblogging.comment.domain.ReComment;
import com.multi.blogging.multiblogging.comment.repository.custom.CustomReCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReCommentRepository extends JpaRepository<ReComment,Long>, CustomReCommentRepository {
}
