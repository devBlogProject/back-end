package com.multi.blogging.multiblogging.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "해당 게시물에 권한이 없습니다.")
public class BoardPermissionDeniedException extends RuntimeException{
}
