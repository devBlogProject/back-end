package com.multi.blogging.multiblogging.board.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardImageUploadRequestDto {

    private MultipartFile image;
}
