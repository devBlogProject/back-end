package com.multi.blogging.multiblogging.sample.controller;

import com.multi.blogging.multiblogging.auth.exception.EmailDuplicateException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/sample")
    public String sample(){
        throw new EmailDuplicateException();

//        return "sample test";
    }
}
