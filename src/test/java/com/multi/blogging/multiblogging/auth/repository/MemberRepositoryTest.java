package com.multi.blogging.multiblogging.auth.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Import(QueryDslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    void save(){
        Member member = Member.builder().email("test@test.com").password("1234").nickName("test").build();
        assertTrue(memberRepository.findAll().isEmpty());
        memberRepository.save(member);
        assertEquals(memberRepository.findAll().toArray().length,1);

        Calendar member_cal =Calendar.getInstance();
        member_cal.setTimeInMillis(member.getCreateDate().getTime());
        Calendar now_cal = Calendar.getInstance();
        now_cal.setTime(new Date());
        assertEquals(member_cal.get(Calendar.YEAR),now_cal.get(Calendar.YEAR) );
        assertEquals(member_cal.get(Calendar.DAY_OF_MONTH),now_cal.get(Calendar.DAY_OF_MONTH) );
        assertEquals(member_cal.get(Calendar.HOUR),now_cal.get(Calendar.HOUR) );

        throw new RuntimeException();
    }

    @Test
    void findOneByEmail(){
        String testEmail="test@test.com";
        Member member =Member.builder().email(testEmail).password("1234").nickName("test").build();
        memberRepository.save(member);
        Member findMember=memberRepository.findOneByEmail(testEmail).get();

        assertEquals(member,findMember);
    }

    @Test
    void 이메일중복체크(){
        String testEmail="test@test.com";
        Member member =Member.builder().email(testEmail).password("1234").nickName("test").build();
        memberRepository.save(member);

        assertTrue(memberRepository.existsByEmail(testEmail));
        assertFalse(memberRepository.existsByEmail("notexistEmail@test.com"));
    }

    @Test
    void 닉네임중복체크(){
        String testEmail="test@test.com";
        Member member =Member.builder().email(testEmail).password("1234").nickName("test").build();
        memberRepository.save(member);

        assertTrue(memberRepository.existsByNickName("test"));
        assertFalse(memberRepository.existsByNickName("test1"));
    }

    @Test
    void findByNickNameStartsWith(){
        String props="test";
        Member member1 =Member.builder().email("test1@test.com").password("1234").nickName(props).build();
        Member member2 =Member.builder().email("test2@test.com").password("1234").nickName(props+"123").build();
        Member member3 =Member.builder().email("test3@test.com").password("1234").nickName(props+"223").build();
        Member member4 =Member.builder().email("test4@test.com").password("1234").nickName("123"+props).build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> findMembers = memberRepository.findByNickNameStartsWith(props);
        assertEquals(findMembers.size(),3);
    }
}