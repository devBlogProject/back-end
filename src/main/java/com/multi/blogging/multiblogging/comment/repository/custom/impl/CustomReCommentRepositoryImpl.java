package com.multi.blogging.multiblogging.comment.repository.custom.impl;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.domain.QMember;
import com.multi.blogging.multiblogging.comment.domain.ReComment;
import com.multi.blogging.multiblogging.comment.repository.custom.CustomReCommentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import static com.multi.blogging.multiblogging.comment.domain.QReComment.reComment;
import static com.multi.blogging.multiblogging.auth.domain.QMember.member;

@RequiredArgsConstructor
@Component
@Qualifier("customReCommentRepositoryImpl")
public class CustomReCommentRepositoryImpl implements CustomReCommentRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<ReComment> findByIdWithMember(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(reComment)
                .join(reComment.member, member).fetchJoin()
                .where(reComment.id.eq(id))
                .fetchOne());
    }
}
