package com.multi.blogging.multiblogging.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardRequestDto {
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;

}
