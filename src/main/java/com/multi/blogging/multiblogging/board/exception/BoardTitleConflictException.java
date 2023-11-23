package com.multi.blogging.multiblogging.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "카테고리 안에 동일한 제목이 존재합니다.")
public class BoardTitleConflictException extends RuntimeException{
}
