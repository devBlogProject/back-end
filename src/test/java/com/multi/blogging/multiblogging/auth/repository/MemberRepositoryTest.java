package com.multi.blogging.multiblogging.auth.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Test
    void save(){
        Member member = Member.builder().memberEmail("test@test.com").password("1234").nickName("test").build();
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
    }

    @Test
    void findOneByMemberEmail(){
        String testEmail="test@test.com";
        Member member =Member.builder().memberEmail(testEmail).password("1234").nickName("test").build();
        memberRepository.save(member);
        Member findMember=memberRepository.findOneByMemberEmail(testEmail).get();

        assertEquals(member,findMember);
    }
}