package com.multi.blogging.multiblogging.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "게시물이 존재하지 않습니다.")
public class BoardNotFoundException extends  RuntimeException{
}
