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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @GetMapping("/board/{board_id}")
    ApiResponse<List<CommentResponseDto>> getComments(@PathVariable("board_id") Long boardId){
        List<Comment> comments = commentService.getComments(boardId);
        return ApiResponse.createSuccess(comments.stream().map(CommentResponseDto::of).toList());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<CommentResponseDto> writeComment(@Valid @RequestBody CommentRequestDto commentRequestDto, Authentication authentication){
        Comment comment = commentService.writeComment(commentRequestDto.getBoardId(), commentRequestDto.getContent(),authentication.getName());

        return ApiResponse.createSuccess(CommentResponseDto.of(comment));
    }

    @PatchMapping("/{comment_id}")
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
