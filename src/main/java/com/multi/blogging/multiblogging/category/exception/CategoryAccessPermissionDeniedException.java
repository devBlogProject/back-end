package com.multi.blogging.multiblogging.category.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "해당 카테고리에 권한이 없습니다.")
public class CategoryAccessPermissionDeniedException extends RuntimeException{
}
