package com.multi.blogging.multiblogging.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UpdateProfileImageRequestDto {
    final String invalidMessage = "URL 형식으로 입력해주세요.";

    @URL(message = invalidMessage)
    @NotBlank(message = invalidMessage)
    public String imageUrl;
}
