package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.MemberSignUpResponseDto;
import com.multi.blogging.multiblogging.auth.dto.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.dto.TokenDto;
import com.multi.blogging.multiblogging.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

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

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reIssue(@RequestBody String refreshToken){

        return ResponseEntity.ok(authService.reIssue(refreshToken));
    }
}
