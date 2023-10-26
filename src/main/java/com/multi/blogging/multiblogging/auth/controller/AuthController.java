package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.request.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.TokenDto;
import com.multi.blogging.multiblogging.auth.dto.request.TokenReIssueRequestDto;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;


    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto
    ){
        return ResponseEntity.ok(authService.login(memberLoginRequestDto));
    }



    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> reIssue(@RequestBody TokenReIssueRequestDto dto) throws AccessDeniedException {
        if (!tokenProvider.validateToken(dto.getRefreshToken())){
            throw new org.springframework.security.access.AccessDeniedException("자격 증명에 실패하였습니다.");
        }

        return ResponseEntity.ok(authService.reIssue(dto.getRefreshToken()));
    }
}
