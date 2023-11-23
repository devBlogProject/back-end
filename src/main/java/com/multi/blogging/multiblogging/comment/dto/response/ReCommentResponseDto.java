package com.multi.blogging.multiblogging.comment.dto.response;

import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.base.BaseResponseDto;
import com.multi.blogging.multiblogging.comment.domain.ReComment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReCommentResponseDto extends BaseResponseDto {
    private Long id;
    private MemberResponseDto author;
    private String content;

    @Builder
    public ReCommentResponseDto(Long id, MemberResponseDto memberResponseDto, String content, LocalDateTime createdDate, LocalDateTime updatedDate){
        this.id=id;
        this.author = memberResponseDto;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    public static ReCommentResponseDto of(ReComment reComment){
        return ReCommentResponseDto.builder()
                .id(reComment.getId())
                .memberResponseDto(MemberResponseDto.of(reComment.getMember()))
                .content(reComment.getContent())
                .createdDate(reComment.getCreatedDate())
                .updatedDate(reComment.getUpdatedDate())
                .build();
    }
}
