package com.multi.blogging.multiblogging.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "코드가 유효하지 않습니다.")
public class MailCodeNotMatchingException extends RuntimeException{
}
