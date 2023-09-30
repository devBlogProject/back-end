package com.multi.blogging.multiblogging.auth.config;

import com.multi.blogging.multiblogging.auth.jwt.JwtAccessDeniedHandler;
import com.multi.blogging.multiblogging.auth.jwt.JwtAuthenticationEntryPoint;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.oAuth.OAuth2LoginFailureHandler;
import com.multi.blogging.multiblogging.auth.oAuth.OAuth2LoginSuccessHandler;
import com.multi.blogging.multiblogging.auth.service.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2UserServiceImpl oAuth2UserService;


//    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/member/signup").permitAll()
                        .requestMatchers("/member/email/**").permitAll()
                        .requestMatchers("/member/nickname/**").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/refresh").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/email/**").permitAll()
//                        .requestMatchers("/sample").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
//                //== 소셜 로그인 설정 ==//
//                .oauth2Login(Customizer.withDefaults())
//                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
//                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
//                .userInfoEndpoint().userService(oAuth2UserService) // customUserzService 설정

                .oauth2Login(oauth2 -> oauth2
//                                .authorizationEndpoint(authorizationEndpointConfig ->
//                                        authorizationEndpointConfig
//                                                .baseUri("/oauth2/authorize"))
//                                .redirectionEndpoint(redirectionEndpointConfig ->
//                                        redirectionEndpointConfig
//                                                .baseUri("/login/oauth2/callback/**"))
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService)) // oauth2 로그인 성공 후 가져올 때의 설정들
                        //
                )
                .exceptionHandling(c -> c.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler))
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
