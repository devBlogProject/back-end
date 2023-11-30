package com.multi.blogging.multiblogging.board.repository.custom.impl;

import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.repository.custom.CustomBoardRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.multi.blogging.multiblogging.board.domain.QBoard.board;
import static com.multi.blogging.multiblogging.auth.domain.QMember.member;
import static com.multi.blogging.multiblogging.category.domain.QCategory.category;
import static com.multi.blogging.multiblogging.comment.domain.QComment.comment;

@RequiredArgsConstructor
@Component
@Qualifier("customBoardRepositoryImpl")
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Board> findByIdWithCategory(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(board)
                .leftJoin(board.category, category).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne());
    }

    @Override
    public Optional<Board> findByIdWithCategoryAndMember(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(board)
                .leftJoin(board.category, category).fetchJoin()
                .leftJoin(board.author, member).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne());
    }

    @Override
    public Optional<Board> findByIdWithMember(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(board)
                        .leftJoin(board.author, member).fetchJoin()
                        .where(board.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Board> findByMemberNicknameAndPostNumberWithMember(String memberNickname, int postNum) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(board)
                        .leftJoin(board.author, member).fetchJoin()
                        .where(board.author.nickName.eq(memberNickname)
                                .and(board.postNumber.eq(postNum))
                        ).fetchOne()
        );
    }

    @Override
    public Slice<Board> findSliceWithMember(Pageable pageable) {
        List<Board> boardList = jpaQueryFactory.selectFrom(board)
                .leftJoin(board.author, member).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .fetch();

        boolean hasNext = false;
        if (boardList.size() > pageable.getPageSize()) {
            boardList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(boardList, pageable, hasNext);
    }


    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder<>(Board.class, "board");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }

}
