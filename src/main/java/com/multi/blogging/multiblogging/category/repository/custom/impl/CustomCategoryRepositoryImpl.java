package com.multi.blogging.multiblogging.category.repository.custom.impl;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.domain.QMember;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.repository.custom.CustomCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.multi.blogging.multiblogging.category.domain.QCategory.category;
import static com.multi.blogging.multiblogging.auth.domain.QMember.member;

@RequiredArgsConstructor
@Component
@Qualifier("customCategoryRepositoryImpl")
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Category> findByIdWithMember(Member member, Long categoryId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(category)
                .from(category, QMember.member)
                .where(category.id.eq(categoryId)
                        .and(category.member.eq(member))
                ).fetchOne());

    }

    @Override
    public List<Category> findTopCategoriesWithMember(Member member) {
        return queryFactory
                .selectFrom(category)
                .from(category, QMember.member)
                .where(
                        category.member.eq(member)
                        .and(category.parent.isNull())
                )
                .fetch();
    }
}
