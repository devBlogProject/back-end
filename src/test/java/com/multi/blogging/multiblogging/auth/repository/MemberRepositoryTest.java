package com.multi.blogging.multiblogging.auth.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.SocialType;
import com.multi.blogging.multiblogging.auth.repository.custom.CustomMemberRepository;
import com.multi.blogging.multiblogging.auth.repository.custom.impl.CustomMemberRepositoryImpl;
import com.multi.blogging.multiblogging.config.QueryDslTestConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Import(QueryDslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    void save() {
        Member member = Member.builder().email(TEST_EMAIL).password(TEST_PASSWORD).nickName(TEST_NICK).build();
        assertTrue(memberRepository.findAll().isEmpty());
        memberRepository.save(member);
        assertEquals(memberRepository.findAll().toArray().length, 1);
    }

    @Test
    void findOneByEmailAndSocialTypeIsNull_소셜멤버() {
        Member socialMember = Member.builder().email(TEST_EMAIL).password(TEST_PASSWORD).nickName(TEST_NICK).socialType(SocialType.GOOGLE).build();
        memberRepository.save(socialMember);
        assertEquals(Optional.empty(), memberRepository.findOneByEmailAndSocialTypeIsNull(TEST_EMAIL));
    }

    @Test
    void findOneByEmailAndSocialTypeIsNull_비소셜멤버() {
        Member nonSocialMember = Member.builder().email(TEST_EMAIL).password(TEST_PASSWORD).nickName(TEST_NICK).build();
        memberRepository.save(nonSocialMember);
        assertEquals(Optional.of(nonSocialMember), memberRepository.findOneByEmailAndSocialTypeIsNull(TEST_EMAIL));

    }

    @Test
    void memberAuditingTest() {
        Member member = Member.builder().email(TEST_EMAIL).password("1234").nickName(TEST_NICK).build();
        memberRepository.save(member);

        Member findMember = memberRepository.findOneByEmail(TEST_EMAIL).orElseThrow();


        assertNotNull(findMember.getCreatedDate());
        assertNotNull(findMember.getUpdatedDate());


        var originalUpdatedDate = findMember.getUpdatedDate();
        findMember.setNickName("test1");
        memberRepository.save(findMember);

        Member newMember = memberRepository.findOneByEmail(TEST_EMAIL).orElseThrow();

        assertNotEquals(originalUpdatedDate, newMember.getUpdatedDate());

    }

    @Test
    void findOneByEmail() {
        String testEmail = TEST_EMAIL;
        Member member = Member.builder().email(testEmail).password("1234").nickName(TEST_NICK).build();
        memberRepository.save(member);
        Member findMember = memberRepository.findOneByEmail(testEmail).get();

        assertEquals(member, findMember);
    }

    @Test
    void 이메일중복체크() {
        String testEmail = TEST_EMAIL;
        Member member = Member.builder().email(testEmail).password("1234").nickName(TEST_NICK).build();
        memberRepository.save(member);

        assertTrue(memberRepository.existsByEmail(testEmail));
        assertFalse(memberRepository.existsByEmail("notexistEmail@test.com"));
    }

    @Test
    void 닉네임중복체크() {
        String testEmail = TEST_EMAIL;
        Member member = Member.builder().email(testEmail).password(TEST_PASSWORD).nickName(TEST_NICK).build();
        memberRepository.save(member);

        assertTrue(memberRepository.existsByNickName(TEST_NICK));
        assertFalse(memberRepository.existsByNickName("test1"));
    }

    @Test
    void findByNickName() {
        Member member = Member.builder().email(TEST_EMAIL).password(TEST_PASSWORD).nickName(TEST_NICK).build();
        memberRepository.save(member);

        assertTrue(memberRepository.findByNickName(TEST_NICK).isPresent());
    }

    @Test
    void findByNickNameStartsWith() {
        String props = "test";
        Member member1 = Member.builder().email("test1@test.com").password("1234").nickName(props).build();
        Member member2 = Member.builder().email("test2@test.com").password("1234").nickName(props + "123").build();
        Member member3 = Member.builder().email("test3@test.com").password("1234").nickName(props + "223").build();
        Member member4 = Member.builder().email("test4@test.com").password("1234").nickName("123" + props).build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> findMembers = memberRepository.findByNickNameStartsWith(props);
        assertEquals(findMembers.size(), 3);
    }
}