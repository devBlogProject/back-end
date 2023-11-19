package com.multi.blogging.multiblogging.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotNull(message = "게시물을 정해주세요.")
    Long boardId;

    @NotEmpty(message = "댓글을 입력해주세요.")
    String content;

}
