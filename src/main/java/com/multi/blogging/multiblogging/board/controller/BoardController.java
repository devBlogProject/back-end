package com.multi.blogging.multiblogging.board.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardImageUploadRequestDto;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.dto.response.BoardResponseDto;
import com.multi.blogging.multiblogging.board.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BoardResponseDto> writeBoard(@RequestPart(required = false)   MultipartFile thumbnail,
                                                    @Valid @RequestPart BoardRequestDto boardRequestDto){
        Board writedBoard = boardService.writeBoard(boardRequestDto, thumbnail);

        return ApiResponse.createSuccess(BoardResponseDto.of(writedBoard));
    }


    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> uploadimage(@Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 image 입니다.")
                                           @ModelAttribute BoardImageUploadRequestDto boardImageUploadRequestDto) {
        String imageUrl = boardService.uploadImage(boardImageUploadRequestDto.getImage());
        return ApiResponse.createSuccess(imageUrl);
    }
}
