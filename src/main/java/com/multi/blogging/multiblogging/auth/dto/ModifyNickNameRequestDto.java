package com.multi.blogging.multiblogging.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifyNickNameRequestDto {

    @NotNull(message = "변경할 닉네임을 입력해주세요.")
    @NotBlank(message = "변경할 닉네임을 입력해주세요.")
    private String nickName;
}
