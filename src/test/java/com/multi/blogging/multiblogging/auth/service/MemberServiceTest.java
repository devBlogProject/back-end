package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.UpdateProfileImageRequestDto;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.auth.repository.RefreshTokenRepository;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    ImageUploadService imageUploadService;




    @Test
    @Transactional
    void updateMemberProfileImage(){
        Member member =Member.builder().email("test@test.com")
                .password("1234")
                .authority(Authority.MEMBER)
                .build();
        memberRepository.save(member);
        var requestDto = new UpdateProfileImageRequestDto();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        requestDto.setImage(multipartFile);
        assertNull(member.getImageUrl());

        when(imageUploadService.uploadFile(multipartFile)).thenReturn("http://sample.com");

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = userDetailsService.loadUserByUsername(member.getEmail());
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,"sampleToken",user.getAuthorities()));

        memberService.updateMemberProfileImage(requestDto);

        var findMember =memberRepository.findOneByEmail("test@test.com");
        assertEquals("http://sample.com",findMember.get().getImageUrl());
    }
}
