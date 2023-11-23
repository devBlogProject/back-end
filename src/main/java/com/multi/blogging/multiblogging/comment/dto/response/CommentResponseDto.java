package com.multi.blogging.multiblogging.comment.dto.response;

import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.base.BaseResponseDto;
import com.multi.blogging.multiblogging.board.dto.response.BoardResponseDto;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto extends BaseResponseDto {
    private Long id;

    private MemberResponseDto memberResponseDto;

    private String content;


    @Builder
    public CommentResponseDto(Long id, MemberResponseDto memberResponseDto, String content, LocalDateTime createdDate,LocalDateTime updatedDate){
        this.id=id;
        this.memberResponseDto = memberResponseDto;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    public static CommentResponseDto of(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .memberResponseDto(MemberResponseDto.of(comment.getMember()))
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .build();
    }
}
