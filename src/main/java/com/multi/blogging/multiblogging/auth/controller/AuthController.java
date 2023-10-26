package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.request.MemberLoginRequestDto;
import com.multi.blogging.multiblogging.auth.dto.TokenDto;
import com.multi.blogging.multiblogging.auth.dto.request.TokenReIssueRequestDto;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.service.AuthService;
import com.multi.blogging.multiblogging.base.ApiResponse;
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
    public ApiResponse<TokenDto> login(
            @Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto
    ){
        return ApiResponse.createSuccess(authService.login(memberLoginRequestDto));
    }



    @DeleteMapping("/logout")
    public ApiResponse logout(){

        authService.logout();
        return ApiResponse.createSuccessWithNoContent();
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenDto> reIssue(@RequestBody TokenReIssueRequestDto dto) throws AccessDeniedException {
        if (!tokenProvider.validateToken(dto.getRefreshToken())){
            throw new org.springframework.security.access.AccessDeniedException("자격 증명에 실패하였습니다.");
        }

        return ApiResponse.createSuccess(authService.reIssue(dto.getRefreshToken()));
    }
}
