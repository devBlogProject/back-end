package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.SecurityUtil;
import com.multi.blogging.multiblogging.auth.domain.RefreshToken;
import com.multi.blogging.multiblogging.auth.dto.request.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.TokenDto;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;


    public String logout() {
        String email = SecurityUtil.getCurrentMemberEmail();
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(email);
        refreshToken.ifPresent(refreshTokenRepository::delete);
        return "로그아웃 되었습니다.";
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

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(authentication.getName());
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
