package com.multi.blogging.multiblogging.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "이미 소셜로그인 회원입니다.")
public class SocialMemberDuplicateException extends RuntimeException {
}
