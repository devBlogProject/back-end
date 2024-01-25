package com.multi.blogging.multiblogging.auth.oAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.blogging.multiblogging.auth.domain.RefreshToken;
import com.multi.blogging.multiblogging.auth.dto.TokenDto;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.auth.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    static final String REDIRECT_URL = "http://localhost:5173/authgoogle";
    static final String ACCESS_TOKEN_PARAMETER = "accessToken";
    static final String REFRESH_TOKEN_PARAMETER = "refreshToken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();


//                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
//                                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
//                findUser.authorizeUser();
        loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(oAuth2User.getEmail(), "", oAuth2User.getAuthorities());

        String accessToken = tokenProvider.createAccessToken(authenticationToken);
        String refreshTokenString = tokenProvider.createRefreshToken(authenticationToken);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(authenticationToken.getName());
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(refreshTokenString));
        } else {
            RefreshToken newToken = new RefreshToken(authenticationToken.getName(), refreshTokenString);
            refreshTokenRepository.save(newToken);
        }

//        String result = objectMapper.writeValueAsString(new TokenDto(accessToken, refreshTokenString));
//        response.setContentType("application/json");
//        response.getWriter().write(result);
        String redirectUrl = UriComponentsBuilder
                .fromUriString(REDIRECT_URL)
                .queryParam(ACCESS_TOKEN_PARAMETER, accessToken)
                .queryParam(REFRESH_TOKEN_PARAMETER, refreshTokenString)
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}