package com.multi.blogging.multiblogging.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentUpdateRequestDto {
    @NotEmpty(message = "댓글을 입력해주세요.")
    String content;
}
