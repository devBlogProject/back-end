package com.multi.blogging.multiblogging.category.repository.custom.impl;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.domain.QMember;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.domain.QCategory;
import com.multi.blogging.multiblogging.category.repository.custom.CustomCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.multi.blogging.multiblogging.board.domain.QBoard.board;
import static com.multi.blogging.multiblogging.category.domain.QCategory.category;
import static com.multi.blogging.multiblogging.auth.domain.QMember.member;

@RequiredArgsConstructor
@Component
@Qualifier("customCategoryRepositoryImpl")
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Category> findByIdWithMember(Long categoryId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(category)
                .leftJoin(category.member, member).fetchJoin()
                .where(category.id.eq(categoryId)
                ).fetchOne());

    }

    @Override
    public List<Category> findTopCategoriesWithChildCategoriesByMemberNickname(String nickname) {
        QCategory child = new QCategory("child");

        return queryFactory
                .selectFrom(category)
                .leftJoin(category.childrenCategories, child).fetchJoin()
                .where(category.member.nickName.eq(nickname)
                        .and(category.parent.isNull())
                )
                .fetch();
    }

    @Override
    public List<Category> findTopCategoriesWithBoardByMemberNickname(String nickname) {
        return queryFactory
                .selectFrom(category)
                .leftJoin(category.boards, board).fetchJoin()
                .where(category.member.nickName.eq(nickname)
                        .and(
                                category.parent.isNull()
                        )).fetch();
    }

    @Override
    public Optional<Category> findByIdWithParentCategory(Long id) {
        QCategory parent = new QCategory("parent");
        return Optional.ofNullable(
                queryFactory.selectFrom(category)
                        .leftJoin(category.parent, parent).fetchJoin()
                        .where(category.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Category> findByIdWithChildCategories(Long id) {
        QCategory child = new QCategory("child");
        return Optional.ofNullable(
                queryFactory.selectFrom(category)
                        .leftJoin(category.childrenCategories, child).fetchJoin()
                        .where(category.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Category> findByIdWithMemberAndBoard(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(category)
                        .leftJoin(category.member, member).fetchJoin()
                        .leftJoin(category.boards, board).fetchJoin()
                        .where(category.id.eq(id))
                        .fetchOne()
        );
    }
}
