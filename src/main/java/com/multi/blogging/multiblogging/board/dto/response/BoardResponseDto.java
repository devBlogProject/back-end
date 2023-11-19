package com.multi.blogging.multiblogging.board.dto.response;

import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.base.BaseResponseDto;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.dto.response.CommentResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponseDto extends BaseResponseDto {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Long categoryId;
    private MemberResponseDto author;
//    LocalDateTime createdDate;
//    LocalDateTime updatedTime;

    private List<CommentResponseDto> parentComments = new ArrayList<>();


    @Builder
    public BoardResponseDto(Long id, String title, String content, Long categoryId, String thumbnailUrl, MemberResponseDto authorResponseDto, LocalDateTime createdDate, LocalDateTime updatedDate, List<CommentResponseDto> commentResponseDtoList){
        this.id=id;
        this.title=title;
        this.content = content;
        this.categoryId = categoryId;
        this.thumbnailUrl = thumbnailUrl;
        this.author = authorResponseDto;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.parentComments= commentResponseDtoList;
    }

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryId(board.getCategory().getId())
                .thumbnailUrl(board.getThumbnailUrl())
                .createdDate(board.getCreatedDate())
                .updatedDate(board.getUpdatedDate())
                .authorResponseDto(MemberResponseDto.of(board.getAuthor()))
                .commentResponseDtoList(board.getParentCommentList().stream().map(CommentResponseDto::of).toList())
                .build();
    }
}
