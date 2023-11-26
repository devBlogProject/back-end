package com.multi.blogging.multiblogging.comment.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.domain.ReComment;
import com.multi.blogging.multiblogging.comment.exception.CommentNotFoundException;
import com.multi.blogging.multiblogging.comment.repository.CommentRepository;
import com.multi.blogging.multiblogging.comment.repository.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReCommentService {
    private final ReCommentRepository reCommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public ReComment writeReComment(Long parentId, String content,String memberEmail){
        Member member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        Comment parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
        ReComment reComment= ReComment.builder()
                .content(content)
                .parent(parent)
                .member(member)
                .build();

        return reCommentRepository.save(reComment);
    }

    public ReComment updateReComment(Long id,String content){
        var reComment = reCommentRepository.findByIdWithMember(id).orElseThrow(CommentNotFoundException::new);
        reComment.setContent(content);
        return reComment;
    }

    public void deleteReComment(Long id){
        var reComment = reCommentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        reCommentRepository.delete(reComment);
    }
}
