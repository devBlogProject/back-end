package com.multi.blogging.multiblogging.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MemberSignUpRequestDto {
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일에는 공백이 포함될 수 없습니다.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    @NotBlank(message = "비밀번호에는 공백이 포함될 수 없습니다.")
    private String password;
}
