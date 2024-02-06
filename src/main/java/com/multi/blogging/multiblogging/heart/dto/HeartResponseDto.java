package com.multi.blogging.multiblogging.heart.dto;

import com.multi.blogging.multiblogging.heart.domain.Heart;
import lombok.Data;

@Data
public class HeartResponseDto {
    private Long id;
    private String memberEmail;
    private Long boardId;

    static public HeartResponseDto of(Heart heart){
        HeartResponseDto heartResponseDto = new HeartResponseDto();
        heartResponseDto.setId(heart.getId());
        heartResponseDto.setMemberEmail(heart.getMember().getEmail());
        heartResponseDto.setBoardId(heart.getBoard().getId());

        return heartResponseDto;
    }
}
