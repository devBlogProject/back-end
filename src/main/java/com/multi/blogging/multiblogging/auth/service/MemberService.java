package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.SecurityUtil;
import com.multi.blogging.multiblogging.auth.config.SecurityConfig;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.domain.RefreshToken;
import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.exception.EmailDuplicateException;
import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public MemberResponseDto modifyNickName(ModifyNickNameRequestDto dto){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> member = memberRepository.findOneByMemberEmail(memberEmail);
        if (member.isEmpty()){
            throw new MemberNotFoundException();
        }
        member.get().setNickName(dto.getNickName());
        return MemberResponseDto.of(member.get());
    }

    @Transactional
    public MemberResponseDto getMemberProfile(){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> member = memberRepository.findOneByMemberEmail(memberEmail);
        if (member.isEmpty()){
            throw new MemberNotFoundException();
        }
        return MemberResponseDto.of(member.get());
    }

    public String logout(){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(memberEmail);
        refreshToken.ifPresent(refreshTokenRepository::delete);
        return "로그아웃 되었습니다.";
    }

    @Transactional
    public MemberResponseDto signUp(MemberSignUpRequestDto dto) {
        if (memberRepository.findOneByMemberEmail(dto.getEmail()).isPresent()) {
            log.debug("MemberService.singUp EmailDuplicatedException occur dto.email: {}", dto.getEmail());
            throw new EmailDuplicateException();
        }

        Member member = Member.builder()
                .memberEmail(dto.getEmail())
                .authority(Authority.ROLE_MEMBER)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        memberRepository.save(member);

        return MemberResponseDto.of(member);
    }

    @Transactional
    public TokenDto login(MemberLoginRequestDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshTokenString = tokenProvider.createRefreshToken(authentication);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(refreshTokenString);
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(refreshTokenString));
        } else {
            RefreshToken newToken = new RefreshToken(authenticationToken.getName(), refreshTokenString);
            refreshTokenRepository.save(newToken);
        }

        return new TokenDto(accessToken, refreshTokenString);
    }

    public TokenDto reIssue(String token) throws AccessDeniedException {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            throw new org.springframework.security.access.AccessDeniedException("자격 증명에 실패하였습니다.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.get().getMemberEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        String accessToken = tokenProvider.createAccessToken(authenticationToken);

        return new TokenDto(accessToken, refreshToken.get().getToken());
    }

}
