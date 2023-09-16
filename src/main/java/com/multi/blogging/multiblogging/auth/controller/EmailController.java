package com.multi.blogging.multiblogging.auth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    @PostMapping("/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid @Email(message = "email") String email) {
        System.out.println(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
