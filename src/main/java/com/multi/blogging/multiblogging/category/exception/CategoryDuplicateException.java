package com.multi.blogging.multiblogging.category.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "중복된 카테고리명입니다.")
public class CategoryDuplicateException extends RuntimeException{
}
