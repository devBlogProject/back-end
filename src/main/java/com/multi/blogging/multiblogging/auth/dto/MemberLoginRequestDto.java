package com.multi.blogging.multiblogging.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberLoginRequestDto {
    @NotNull(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    private String password;
}
