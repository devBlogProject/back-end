package com.multi.blogging.multiblogging.comment.repository;

import com.multi.blogging.multiblogging.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
