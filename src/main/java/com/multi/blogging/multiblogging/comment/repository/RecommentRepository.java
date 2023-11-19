package com.multi.blogging.multiblogging.comment.repository;

import com.multi.blogging.multiblogging.comment.domain.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommentRepository extends JpaRepository<ReComment,Long> {
}
