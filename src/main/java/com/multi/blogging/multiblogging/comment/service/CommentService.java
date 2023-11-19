package com.multi.blogging.multiblogging.comment.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.exception.BoardNotFoundException;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.exception.CommentNotFoundException;
import com.multi.blogging.multiblogging.comment.exception.CommentPermissionDeniedException;
import com.multi.blogging.multiblogging.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Comment updateComment(Long commentId,String content){
        var comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.setContent(content);
        return comment;
    }
    @Transactional
    public Comment writeComment(Long boardId,String content){
        Member currentMember = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Comment comment = Comment.builder()
                .board(board)
                .member(currentMember)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }


    @Transactional
    public void deleteComment(Long id){
        var comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }

}
