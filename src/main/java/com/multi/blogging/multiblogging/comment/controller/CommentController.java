package com.multi.blogging.multiblogging.comment.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.dto.request.CommentRequestDto;
import com.multi.blogging.multiblogging.comment.dto.request.CommentUpdateRequestDto;
import com.multi.blogging.multiblogging.comment.dto.response.CommentResponseDto;
import com.multi.blogging.multiblogging.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<CommentResponseDto> writeComment(@Valid @RequestBody CommentRequestDto commentRequestDto){
        Comment comment = commentService.writeComment(commentRequestDto.getBoardId(), commentRequestDto.getContent());

        return ApiResponse.createSuccess(CommentResponseDto.of(comment));
    }

    @PutMapping("/{comment_id}")
    ApiResponse<CommentResponseDto> updateComment(@PathVariable("comment_id")Long commentId,
                                                  @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto
                                                  ){
        Comment updatedComment = commentService.updateComment(commentId, commentUpdateRequestDto.getContent());
        return ApiResponse.createSuccess(CommentResponseDto.of(updatedComment));
    }

    @DeleteMapping("/{comment_id}")
    ApiResponse<?> deleteComment(@PathVariable("comment_id")Long commentId){
        commentService.deleteComment(commentId);

        return ApiResponse.createSuccessWithNoContent();
    }
}
