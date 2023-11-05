package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.config.QueryDslTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Import(QueryDslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDetailsServiceImplTest {


    UserDetailsServiceImpl userDetailsService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestEntityManager em;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(memberRepository);
    }

    @Test
    void loadUserByUserName() {
        String testEmail = TEST_EMAIL;
        Member member = Member.builder()
                .email(testEmail)
                .password(TEST_PASSWORD)
                .nickName(TEST_NICK)
                .authority(Authority.MEMBER)
                .build();
        em.persist(member);

        UserDetails user = userDetailsService.loadUserByUsername(testEmail);

        assertEquals(user.getUsername(),testEmail);
        assertEquals(user.getAuthorities(), user.getAuthorities());

    }
}