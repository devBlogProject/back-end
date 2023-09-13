package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomUserDetailsServiceTest {


    CustomUserDetailsService customUserDetailsService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestEntityManager em;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(memberRepository);
    }

    @Test
    void loadUserByUserName() {
        String testEmail = "test@test.com";
        Member member = Member.builder()
                .memberEmail(testEmail)
                .password("1234")
                .nickName("test")
                .authority(Authority.ROLE_MEMBER)
                .build();
        em.persist(member);

        UserDetails user = customUserDetailsService.loadUserByUsername(testEmail);

        assertEquals(user.getUsername(),testEmail);
        assertEquals(user.getAuthorities(), user.getAuthorities());

    }
}