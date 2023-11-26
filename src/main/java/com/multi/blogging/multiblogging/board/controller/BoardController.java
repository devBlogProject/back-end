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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BoardResponseDto> writeBoard(@RequestPart(required = false)   MultipartFile thumbnail,
                                                    @Valid @RequestPart BoardRequestDto boardRequestDto, Authentication authentication){
        Board writedBoard = boardService.writeBoard(boardRequestDto, thumbnail,authentication.getName());
        return ApiResponse.createSuccess(BoardResponseDto.of(writedBoard));
    }

    @GetMapping("/all")
    public ApiResponse<Slice<BoardResponseDto>> getBoards(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        Slice<Board> boards = boardService.getBoards(pageRequest);
        return ApiResponse.createSuccess(boards.map(BoardResponseDto::of));
    }

    @PutMapping(path = "/{board_id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<BoardResponseDto> updateBoard(@RequestPart(required = false)MultipartFile thumbnail,@PathVariable("board_id") Long boardId,@Valid @RequestPart BoardRequestDto boardRequestDto){
        Board updatedBoard = boardService.updateBoard(boardId,boardRequestDto,thumbnail);

        return ApiResponse.createSuccess(BoardResponseDto.of(updatedBoard));
    }

    @GetMapping("/{board_id}")
    public ApiResponse<BoardResponseDto> getBoard(@PathVariable("board_id") Long boardId){
        Board board = boardService.getBoard(boardId);
        return ApiResponse.createSuccess(BoardResponseDto.of(board));
    }

    @DeleteMapping("/{board_id}")
    public ApiResponse<?> deleteBoard(@PathVariable("board_id") Long boardId){
        boardService.deleteBoard(boardId);
        return ApiResponse.createSuccessWithNoContent();
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> uploadImage(@Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 image 입니다.")
                                           @ModelAttribute BoardImageUploadRequestDto boardImageUploadRequestDto) {
        String imageUrl = boardService.uploadImage(boardImageUploadRequestDto.getImage());
        return ApiResponse.createSuccess(imageUrl);
    }
}
