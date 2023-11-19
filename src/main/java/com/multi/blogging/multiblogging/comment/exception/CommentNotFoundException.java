package com.multi.blogging.multiblogging.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "댓글을 찾을 수 없습니다.")
public class CommentNotFoundException extends RuntimeException{
}
