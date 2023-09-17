package com.multi.blogging.multiblogging.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ModifyPasswordRequestDto {

    @NotNull(message = "기존 비밀번호를 입력해주세요.")
    String oldPassword;

    @NotNull(message = "변경할 비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    String newPassword;
}
