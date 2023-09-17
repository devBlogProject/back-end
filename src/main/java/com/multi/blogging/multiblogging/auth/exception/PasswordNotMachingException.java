package com.multi.blogging.multiblogging.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "비밀번호가 맞지 않습니다.")
public class PasswordNotMachingException extends RuntimeException{
}
