package com.multi.blogging.multiblogging.tempboard.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import com.multi.blogging.multiblogging.tempboard.dto.TempBoardRequestDto;
import com.multi.blogging.multiblogging.tempboard.service.TempBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/temp-board")
public class TempBoardController {

    private final TempBoardService tempBoardService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TempBoard> saveTempBoard(Authentication authentication, @RequestBody TempBoardRequestDto tempBoardRequestDto){
        String title=tempBoardRequestDto.getTitle();
        String content = tempBoardRequestDto.getContent();
        var tempBoard = tempBoardService.saveTempBoard(authentication.getName(), title, content);

        return ApiResponse.createSuccess(tempBoard);
    }

    @GetMapping()
    public ApiResponse<TempBoard> getTempBoard(Authentication authentication){
        var tempBoard=tempBoardService.getTempBoard(authentication.getName());
        return ApiResponse.createSuccess(tempBoard);
    }

    @DeleteMapping()
    public ApiResponse<?> deleteTempBoard(Authentication authentication){
        tempBoardService.deleteTempBoard(authentication.getName());
        return ApiResponse.createSuccessWithNoContent();
    }
}
