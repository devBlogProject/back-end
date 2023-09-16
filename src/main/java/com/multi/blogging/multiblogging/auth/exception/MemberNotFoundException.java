package com.multi.blogging.multiblogging.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "유저를 찾을 수 없습니다.")
public class MemberNotFoundException extends RuntimeException{
}
