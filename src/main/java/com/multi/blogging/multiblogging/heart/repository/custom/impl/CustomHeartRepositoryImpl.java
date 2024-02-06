package com.multi.blogging.multiblogging.heart.repository.custom.impl;

import com.multi.blogging.multiblogging.heart.domain.Heart;
import com.multi.blogging.multiblogging.heart.repository.custom.CustomHeartRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.multi.blogging.multiblogging.auth.domain.QMember.member;
import static com.multi.blogging.multiblogging.board.domain.QBoard.board;
import static com.multi.blogging.multiblogging.heart.domain.QHeart.heart;


@RequiredArgsConstructor
@Qualifier("customHeartRepositoryImpl")
@Component
public class CustomHeartRepositoryImpl implements CustomHeartRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Heart> findByMemberAndBoard(Long memberId, Long boardId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(heart)
                        .where(heart.member.id.eq(memberId)
                                .and(heart.board.id.eq(boardId)))
                        .fetchOne());
    }

    @Override
    public Optional<Heart> findByMemberAndBoardWithMember(Long memberId, Long boardId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(heart)
                        .leftJoin(heart.member, member).fetchJoin()
                        .where(heart.member.id.eq(memberId)
                                .and(heart.board.id.eq(boardId)))
                        .fetchOne());
    }



    @Override
    public int getCountByBoard(Long boardId) {
        return jpaQueryFactory.selectFrom(heart)
                .where(heart.board.id.eq(boardId))
                .fetch().size();
    }
}
