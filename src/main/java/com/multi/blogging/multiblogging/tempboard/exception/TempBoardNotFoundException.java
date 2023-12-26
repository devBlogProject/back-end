package com.multi.blogging.multiblogging.tempboard.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "임시 저장 게시물을 찾을 수 없습니다.")
public class TempBoardNotFoundException extends RuntimeException{
}
