package com.multi.blogging.multiblogging.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "이미 존재하는 닉네임입니다.")
public class NickNameDuplicateException extends RuntimeException{
}
