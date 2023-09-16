package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.jwt.TokenProvider;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PutMapping("/nickname")
    public ResponseEntity<MemberResponseDto> modifyNickName(@Valid @RequestBody ModifyNickNameRequestDto modifyNickNameRequestDto){
        return ResponseEntity.ok(memberService.modifyNickName(modifyNickNameRequestDto));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> getMemberProfile(){
        return ResponseEntity.ok(memberService.getMemberProfile());
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(
            @Valid @RequestBody MemberSignUpRequestDto memberSignUpRequestDto
            ){
        return ResponseEntity.ok(memberService.signUp(memberSignUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto
            ){
        return ResponseEntity.ok(memberService.login(memberLoginRequestDto));
    }


    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok(memberService.logout());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> reIssue(@RequestBody TokenReIssueRequestDto dto) throws AccessDeniedException {
        if (!tokenProvider.validateToken(dto.getRefreshToken())){
            throw new org.springframework.security.access.AccessDeniedException("자격 증명에 실패하였습니다.");
        }

        return ResponseEntity.ok(memberService.reIssue(dto.getRefreshToken()));
    }
}
