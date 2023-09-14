package com.multi.blogging.multiblogging.auth.dto;

import lombok.Data;

@Data
public class TokenReIssueRequestDto {
    private String refreshToken;
}

