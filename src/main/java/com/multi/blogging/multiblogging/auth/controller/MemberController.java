package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.ModifyNickNameRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.ModifyPasswordRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.UpdateProfileImageRequestDto;
import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import com.multi.blogging.multiblogging.base.ApiResponse;
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

    @PatchMapping("/nickname")
    public ApiResponse<MemberResponseDto> modifyNickName(@Valid @RequestBody ModifyNickNameRequestDto modifyNickNameRequestDto) {
        MemberResponseDto dto = MemberResponseDto.of(memberService.modifyNickName(modifyNickNameRequestDto.getNickName()));
        return ApiResponse.createSuccess(dto);
    }

    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MemberResponseDto> updateProfileImage(
            @Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 image 입니다.")
            @ModelAttribute UpdateProfileImageRequestDto updateProfileImageRequestDto) {
        MemberResponseDto dto = MemberResponseDto.of(memberService.updateMemberProfileImage(updateProfileImageRequestDto.getImage()));
        return ApiResponse.createSuccess(dto);
    }

    @PatchMapping("/password")
    public ApiResponse<?> modifyPassword(@Valid @RequestBody ModifyPasswordRequestDto modifyPasswordRequestDto) {
        memberService.modifyPassword(modifyPasswordRequestDto.getOldPassword(), modifyPasswordRequestDto.getNewPassword());
        return ApiResponse.createSuccessWithNoContent();
    }

    @GetMapping("/profile")
    public ApiResponse<MemberResponseDto> getMemberProfile() {
        MemberResponseDto dto = MemberResponseDto.of(memberService.getMemberProfile());
        return ApiResponse.createSuccess(dto);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponseDto> signup(
            @Valid @RequestBody MemberSignUpRequestDto memberSignUpRequestDto
    ) {
        MemberResponseDto dto = MemberResponseDto.of(memberService.signUp(memberSignUpRequestDto));
        return ApiResponse.createSuccess(dto);
    }

    @GetMapping("/email/{email}/exists")
    public ApiResponse<?> checkEmailDuplicate(@PathVariable String email) {
        return ApiResponse.createSuccess(memberService.checkEmailDuplicate(email));
    }

    @GetMapping("/nickname/{nickname}/exists")
    public ApiResponse<?> checkNickNameDuplicate(@PathVariable String nickname) {
        return ApiResponse.createSuccess(memberService.checkNickNameDuplicate(nickname));
    }
}
