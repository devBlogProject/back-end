package com.multi.blogging.multiblogging.board.dto.request;

import com.multi.blogging.multiblogging.category.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardRequestDto {
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;

    @NotBlank(message = "카테고리를 정해주세요.")
    private Category category;

    @NotBlank(message = "내용을 작성해주세요.")
    private String content;
}
