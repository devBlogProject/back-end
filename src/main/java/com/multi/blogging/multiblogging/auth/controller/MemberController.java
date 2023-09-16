package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PutMapping("/nickname")
    public ResponseEntity<MemberResponseDto> modifyNickName(@Valid @RequestBody ModifyNickNameRequestDto modifyNickNameRequestDto){
        return ResponseEntity.ok(memberService.modifyNickName(modifyNickNameRequestDto));
    }

    @PutMapping("/password")
    public ResponseEntity<String> modifyPassword(@Valid @RequestBody ModifyPasswordRequestDto modifyPasswordRequestDto){
        memberService.modifyPassword(modifyPasswordRequestDto);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> getMemberProfile(){
        return ResponseEntity.ok(memberService.getMemberProfile());
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(
            @Valid @RequestBody MemberSignUpRequestDto memberSignUpRequestDto
            ){
        return new ResponseEntity<>(memberService.signUp(memberSignUpRequestDto), HttpStatus.CREATED);
    }


}
