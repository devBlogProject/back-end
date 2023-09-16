package com.multi.blogging.multiblogging.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ModifyPasswordRequestDto {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotNull(message = "이메일을 입력해주세요.")
    String email;

    @NotNull(message = "변경할 비밀번호를 입력해주세요.")
    @Size(min = 4)
    String password;
}
