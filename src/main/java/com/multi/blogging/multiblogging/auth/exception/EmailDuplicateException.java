package com.multi.blogging.multiblogging.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "이미 가입되어 있는 이메일입니다.")
public class EmailDuplicateException extends RuntimeException {
}
