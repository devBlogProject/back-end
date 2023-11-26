package com.multi.blogging.multiblogging.category.dto.response;

import com.multi.blogging.multiblogging.board.domain.Board;
import lombok.Data;

@Data
public class BoardResponseDto {
    Long id;
    String title;

    static BoardResponseDto of(Board board){
        BoardResponseDto responseDto = new BoardResponseDto();
        responseDto.setId(board.getId());
        responseDto.setTitle(board.getTitle());
        return responseDto;
    }
}
