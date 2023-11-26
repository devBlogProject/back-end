package com.multi.blogging.multiblogging.comment.repository.custom.impl;

import com.multi.blogging.multiblogging.auth.domain.QMember;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.repository.custom.CustomCommentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.multi.blogging.multiblogging.auth.domain.QMember.member;
import static com.multi.blogging.multiblogging.comment.domain.QComment.comment;
import static com.multi.blogging.multiblogging.comment.domain.QReComment.reComment;

@RequiredArgsConstructor
@Component
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findByIdWithMember(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(comment)
                        .leftJoin(comment.member,member).fetchJoin()
                        .where(comment.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<Comment> findByBoardIdWithMemberAndReComment(Long boardId) {
        QMember member1 = new QMember("member1");
        QMember member2 = new QMember("member2");
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.children,reComment).fetchJoin()
                .leftJoin(comment.member,member1).fetchJoin()
                .leftJoin(reComment.member,member2).fetchJoin()
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdDate.asc())
                .orderBy(reComment.createdDate.asc())
                .fetch();
    }


}
