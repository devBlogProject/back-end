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
    private int postNum;
    private int viewCount;
    private int likeCount;
//    LocalDateTime createdDate;
//    LocalDateTime updatedTime;



    @Builder
    public BoardResponseDto(Long id, String title, String content, Long categoryId,String thumbnailUrl, MemberResponseDto authorResponseDto, LocalDateTime createdDate, LocalDateTime updatedDate,int postNum,int viewCount,int likeCount){
        this.id=id;
        this.title=title;
        this.content = content;
        this.categoryId = categoryId;
        this.thumbnailUrl = thumbnailUrl;
        this.author = authorResponseDto;
        this.postNum=postNum;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.viewCount=viewCount;
        this.likeCount=likeCount;
    }

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryId(board.getCategory().getId())
                .thumbnailUrl(board.getThumbnailUrl())
                .postNum(board.getPostNumber())
                .createdDate(board.getCreatedDate())
                .updatedDate(board.getUpdatedDate())
                .authorResponseDto(MemberResponseDto.of(board.getAuthor()))
                .viewCount(board.getViewCount())
                .build();
    }
}
