package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.service.EmailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@Validated
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    @PostMapping("/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid @Email(message = "이메일 형식으로 입력해주세요.") String email) throws Exception {
        String code = emailService.createCode();
        emailService.sendAuthCodeEmail(email,"Multiblogging 인증코드",code);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
