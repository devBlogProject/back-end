package com.multi.blogging.multiblogging.comment.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.comment.domain.ReComment;
import com.multi.blogging.multiblogging.comment.dto.request.ReCommentRequestDto;
import com.multi.blogging.multiblogging.comment.dto.response.ReCommentResponseDto;
import com.multi.blogging.multiblogging.comment.service.ReCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/re-comment")
@RequiredArgsConstructor
public class ReCommentController {
    private final ReCommentService reCommentService;

    @PostMapping("/parent/{parent_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReCommentResponseDto> writeReComment(@PathVariable("parent_id") Long parentId, @Valid @RequestBody ReCommentRequestDto reCommentRequestDto) {
        ReComment reComment= reCommentService.writeReComment(parentId, reCommentRequestDto.getContent());
        return ApiResponse.createSuccess(ReCommentResponseDto.of(reComment));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ReCommentResponseDto> updateReComment(@PathVariable("id") Long id,@Valid @RequestBody ReCommentRequestDto reCommentRequestDto){
        ReComment reComment = reCommentService.updateReComment(id, reCommentRequestDto.getContent());
        return ApiResponse.createSuccess(ReCommentResponseDto.of(reComment));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteReComment(@PathVariable("id") Long id){
        reCommentService.deleteReComment(id);
        return ApiResponse.createSuccessWithNoContent();
    }
}
