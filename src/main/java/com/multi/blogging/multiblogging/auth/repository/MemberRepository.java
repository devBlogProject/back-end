package com.multi.blogging.multiblogging.auth.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
}