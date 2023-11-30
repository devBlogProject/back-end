package com.multi.blogging.multiblogging.heart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "해당 좋아요를 찾을 수 없습니다.", value = HttpStatus.NOT_FOUND)
public class HeartNotFoundException extends RuntimeException{
}
