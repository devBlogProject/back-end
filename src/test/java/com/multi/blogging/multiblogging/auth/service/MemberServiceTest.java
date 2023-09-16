package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Test
    void logout(){
        Member signUpMember= Member.builder().memberEmail("test@test.com")
                .password("1234")
                .build();
        MemberSignUpRequestDto signUpRequestDto= new MemberSignUpRequestDto();
        signUpRequestDto.setEmail(signUpMember.getMemberEmail());
        signUpRequestDto.setPassword(signUpMember.getPassword());
        memberService.signUp(signUpRequestDto);

        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto();
        memberLoginRequestDto.setEmail(signUpMember.getMemberEmail());
        memberLoginRequestDto.setPassword(signUpMember.getPassword());

        memberService.login(memberLoginRequestDto);
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = userDetailsService.loadUserByUsername(signUpMember.getMemberEmail());
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,"sampleToken",user.getAuthorities()));
        assertNotNull(
                refreshTokenRepository.findById(signUpMember.getMemberEmail())
        );
        memberService.logout();

        assertEquals(refreshTokenRepository.findById(signUpMember.getMemberEmail()), Optional.empty());
    }
}