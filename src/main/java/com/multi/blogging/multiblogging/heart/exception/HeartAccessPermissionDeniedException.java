package com.multi.blogging.multiblogging.heart.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "해당 댓글에 권한이 없습니다.")
public class HeartAccessPermissionDeniedException extends RuntimeException{
}
