package com.multi.blogging.multiblogging.heart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "이미 좋아요를 누른 게시물입니다.",value = HttpStatus.CONFLICT)
public class HeartConflictException extends RuntimeException{
}
