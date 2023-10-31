package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.dto.TokenDto;
import com.multi.blogging.multiblogging.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AuthServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    MockMvc mockMvc;

    static final String TEST_EMAIL = "test@test.com";
    static final String TEST_PASSWORD = "1234";


    @BeforeEach
    void setUp() {
        Member signUpMember = Member.builder().email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
        MemberSignUpRequestDto signUpRequestDto = new MemberSignUpRequestDto();
        signUpRequestDto.setEmail(signUpMember.getEmail());
        signUpRequestDto.setPassword(signUpMember.getPassword());
        memberService.signUp(signUpRequestDto);
    }

    @Test
    @Transactional
    void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sample")).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void login() throws Exception {
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto();
        memberLoginRequestDto.setEmail(TEST_EMAIL);
        memberLoginRequestDto.setPassword(TEST_PASSWORD);

        TokenDto tokenDto = authService.login(memberLoginRequestDto);
        setAuthentication();

        mockMvc.perform(MockMvcRequestBuilders.get("/sample").header("Authorization",tokenDto.getAccessToken())).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void logout() {
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto();
        memberLoginRequestDto.setEmail(TEST_EMAIL);
        memberLoginRequestDto.setPassword(TEST_PASSWORD);

        authService.login(memberLoginRequestDto);
        setAuthentication();
        assertNotNull(
                refreshTokenRepository.findById(TEST_EMAIL)
        );
        authService.logout();

        assertEquals(refreshTokenRepository.findById(TEST_PASSWORD), Optional.empty());
    }

    @Test
    @Transactional
    void reIssue() throws AccessDeniedException, InterruptedException {
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto();
        memberLoginRequestDto.setEmail(TEST_EMAIL);
        memberLoginRequestDto.setPassword(TEST_PASSWORD);

        TokenDto tokenDto = authService.login(memberLoginRequestDto);
        setAuthentication();

        Thread.sleep(1000);

        TokenDto reIssuedTokenDto = authService.reIssue(tokenDto.getRefreshToken());

        assertNotEquals(tokenDto.getAccessToken(), reIssuedTokenDto.getAccessToken());
        assertEquals(tokenDto.getRefreshToken(), reIssuedTokenDto.getRefreshToken());
    }

    private void setAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = userDetailsService.loadUserByUsername(TEST_EMAIL);
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, TEST_PASSWORD, user.getAuthorities()));
    }
}