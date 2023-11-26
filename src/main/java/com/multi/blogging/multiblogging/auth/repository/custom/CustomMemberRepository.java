package com.multi.blogging.multiblogging.auth.repository.custom;

import com.multi.blogging.multiblogging.auth.domain.Member;

import java.util.List;
import java.util.Optional;

public interface CustomMemberRepository {
    List<Member> findByNickNameStartsWith(String nickName);

    Optional<Member> findOneByEmailWithCategories(String email);
}
