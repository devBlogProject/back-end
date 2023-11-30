package com.multi.blogging.multiblogging.heart.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/board/{board_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> insert(@PathVariable("board_id") Long boardId){
        heartService.insert(SecurityUtil.getCurrentMemberEmail(), boardId);
        return ApiResponse.createSuccessWithNoContent();
    }

    @DeleteMapping("/board/{board_id}")
    public ApiResponse<?> delete(@PathVariable("board_id") Long boardId){
        heartService.delete(SecurityUtil.getCurrentMemberEmail(), boardId);
        return ApiResponse.createSuccessWithNoContent();
    }
}
