package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.response.EmailVerificationResponseDto;
import com.multi.blogging.multiblogging.auth.service.EmailService;
import com.multi.blogging.multiblogging.base.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@Validated
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    @PostMapping("/verification-requests")
    public ApiResponse<?> sendMessage(@RequestParam("email") @Valid @Email(message = "이메일 형식으로 입력해주세요.") String email) throws Exception {
        String code = emailService.createCode();
        emailService.sendAuthCodeEmail(email,"Multiblogging 인증코드",code);
        return ApiResponse.createSuccessWithNoContent();
    }

    @GetMapping("/verifications")
    public ApiResponse<EmailVerificationResponseDto> verifyEmail(@RequestParam("email") @Valid @Email String email,
                                                                    @RequestParam("code") String authCode) {

        return ApiResponse.createSuccess(emailService.verifiedCode(email,authCode));
    }
}
