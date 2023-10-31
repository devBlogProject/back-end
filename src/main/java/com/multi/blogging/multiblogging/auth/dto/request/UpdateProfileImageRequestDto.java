package com.multi.blogging.multiblogging.auth.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileImageRequestDto {
    private MultipartFile image;
}
