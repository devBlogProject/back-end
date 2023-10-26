package com.multi.blogging.multiblogging.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "카테고리명을 입력해주세요.")
    private String title;

    private String parentCategoryTitle;
}
