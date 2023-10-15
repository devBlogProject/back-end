package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PutMapping(value="/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponseDto> updateProfileImage(
            @Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 image 입니다.")
            @ModelAttribute UpdateProfileImageRequestDto updateProfileImageRequestDto){
        return ResponseEntity.ok(memberService.updateMemberProfileImage(updateProfileImageRequestDto));
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

    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }

    @GetMapping("/nickname/{nickname}/exists")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickname){
        return ResponseEntity.ok(memberService.checkNickNameDuplicate(nickname));
    }
}
