package com.multi.blogging.multiblogging.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    @NotBlank(message = "카테고리명을 입력해주세요.")
    private String title;

}
