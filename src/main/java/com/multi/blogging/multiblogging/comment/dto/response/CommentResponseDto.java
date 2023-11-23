package com.multi.blogging.multiblogging.comment.dto.response;

import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.base.BaseResponseDto;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponseDto extends BaseResponseDto {
    private Long id;

    private MemberResponseDto author;

    private String content;

    private List<ReCommentResponseDto> reComments;


    @Builder
    public CommentResponseDto(Long id, MemberResponseDto memberResponseDto, String content,List<ReCommentResponseDto> reComments, LocalDateTime createdDate,LocalDateTime updatedDate){
        this.id=id;
        this.author = memberResponseDto;
        this.content = content;
        this.reComments = reComments;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    public static CommentResponseDto of(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .memberResponseDto(MemberResponseDto.of(comment.getMember()))
                .content(comment.getContent())
                .reComments(comment.getChildren().stream().map(ReCommentResponseDto::of).toList())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .build();
    }
}
