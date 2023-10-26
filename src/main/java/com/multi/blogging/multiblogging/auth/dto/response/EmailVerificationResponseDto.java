package com.multi.blogging.multiblogging.auth.dto.response;

import lombok.Data;

@Data
public class EmailVerificationResponseDto {
    private String temporaryPassword;
}
