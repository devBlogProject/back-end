package com.multi.blogging.multiblogging.auth.repository.custom.impl;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.custom.CustomMemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.multi.blogging.multiblogging.auth.domain.QMember.member;

@RequiredArgsConstructor
@Component
@Qualifier("customMemberRepositoryImpl")
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Member> findByNickNameStartsWith(String nickName) {
        return queryFactory
                .selectFrom(member)
                .where(member.nickName.startsWith(nickName))
                .fetch();
    }
}
