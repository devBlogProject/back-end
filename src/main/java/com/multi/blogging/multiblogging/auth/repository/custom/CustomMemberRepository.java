package com.multi.blogging.multiblogging.auth.repository.custom;

import com.multi.blogging.multiblogging.auth.domain.Member;

import java.util.List;

public interface CustomMemberRepository {
    List<Member> findByNickNameStartsWith(String nickName);
}
