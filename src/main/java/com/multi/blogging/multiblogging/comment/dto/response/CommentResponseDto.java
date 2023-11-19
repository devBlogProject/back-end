package com.multi.blogging.multiblogging.comment.dto.response;

import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.base.BaseResponseDto;
import com.multi.blogging.multiblogging.board.dto.response.BoardResponseDto;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponseDto extends BaseResponseDto {
    private Long id;

    private MemberResponseDto memberResponseDto;

    private String content;

    public static CommentResponseDto of(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .memberResponseDto(MemberResponseDto.of(comment.getMember()))
                .content(comment.getContent())
                .build();
    }
}
