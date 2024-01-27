package com.multi.blogging.multiblogging.auth.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.SocialType;
import com.multi.blogging.multiblogging.auth.repository.custom.CustomMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findOneByEmail(String email);

//    @Query("select m from Member m where m.nickName like :nickName%")
//    List<Member> findByNickNameStartsWith(String nickName);

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType,String socialId);

    Optional<Member> findOneByEmailAndSocialTypeIsNull(String email);

    Optional<Member> findByNickName(String nickname);
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
}