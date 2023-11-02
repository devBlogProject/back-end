package com.multi.blogging.multiblogging.board.dto.request;

import com.multi.blogging.multiblogging.category.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardRequestDto {
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;

    @NotBlank(message = "내용을 작성해주세요.")
    private String content;

    @NotBlank(message = "카테고리를 정해주세요.")
    private Long categoryId;

    MultipartFile thumbnailPicture;
}
