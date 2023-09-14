package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.HttpResource;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponseDto> signup(
            @Valid @RequestBody MemberSignUpRequestDto memberSignUpRequestDto
            ){
        return ResponseEntity.ok(authService.signUp(memberSignUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto
            ){
        return ResponseEntity.ok(authService.login(memberLoginRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> reIssue(@RequestBody TokenReIssueRequestDto dto) throws AccessDeniedException {
        if (!tokenProvider.validateToken(dto.getRefreshToken())){
            throw new org.springframework.security.access.AccessDeniedException("자격 증명에 실패하였습니다.");
        }

        return ResponseEntity.ok(authService.reIssue(dto.getRefreshToken()));
    }
}
