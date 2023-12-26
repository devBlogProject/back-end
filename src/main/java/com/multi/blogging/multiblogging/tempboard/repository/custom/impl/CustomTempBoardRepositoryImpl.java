package com.multi.blogging.multiblogging.tempboard.repository.custom.impl;

import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import com.multi.blogging.multiblogging.tempboard.repository.custom.CustomTempBoardRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.multi.blogging.multiblogging.auth.domain.QMember.member;
import static com.multi.blogging.multiblogging.tempboard.domain.QTempBoard.tempBoard;

@RequiredArgsConstructor
@Component
@Qualifier("customTempBoardRepositoryImpl")
public class CustomTempBoardRepositoryImpl implements CustomTempBoardRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<TempBoard> findByAuthorEmail(String email) {
        return Optional.ofNullable(queryFactory
                .selectFrom(tempBoard)
                .where(tempBoard.author.email.eq(email))
                .fetchOne()
        );
    }
}
