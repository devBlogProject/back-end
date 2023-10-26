package com.multi.blogging.multiblogging.category.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "카테고리가 존재하지 않습니다.")
public class CategoryNotFoundException extends RuntimeException{
}
