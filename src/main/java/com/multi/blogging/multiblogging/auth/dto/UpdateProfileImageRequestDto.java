package com.multi.blogging.multiblogging.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UpdateProfileImageRequestDto {

    @URL(message = "URL형식으로 입력해주세요.")
    @NotBlank(message = "URL형식으로 입력해주세요.")
    public String imageUrl;
}
