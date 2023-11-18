package com.multi.blogging.multiblogging.board.dto.response;

import com.multi.blogging.multiblogging.auth.dto.response.MemberResponseDto;
import com.multi.blogging.multiblogging.board.domain.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Long categoryId;
    private MemberResponseDto author;
    LocalDateTime createdDate;
    LocalDateTime updatedTime;

    // private List<Comment> parentComments = new ArrayList<Comment>();


    @Builder
    public BoardResponseDto(Long id, String title, String content, Long categoryId, String thumbnailUrl, MemberResponseDto authorResponseDtoResponseDto, LocalDateTime createdDate, LocalDateTime updatedTime){
        this.id=id;
        this.title=title;
        this.content = content;
        this.categoryId = categoryId;
        this.thumbnailUrl = thumbnailUrl;
        this.author = authorResponseDtoResponseDto;
        this.createdDate = createdDate;
        this.updatedTime = updatedTime;
    }

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryId(board.getCategory().getId())
                .thumbnailUrl(board.getThumbnailUrl())
                .createdDate(board.getCreatedDate())
                .updatedTime(board.getUpdatedDate())
                .authorResponseDtoResponseDto(MemberResponseDto.of(board.getAuthor()))
                .build();
    }
}
