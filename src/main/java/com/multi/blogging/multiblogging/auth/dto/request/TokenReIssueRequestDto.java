package com.multi.blogging.multiblogging.auth.dto.request;

import lombok.Data;

@Data
public class TokenReIssueRequestDto {
    private String refreshToken;
}

