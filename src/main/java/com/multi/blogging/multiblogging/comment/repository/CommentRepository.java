package com.multi.blogging.multiblogging.comment.repository;

import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.repository.custom.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>, CustomCommentRepository {
}
