package com.multi.blogging.multiblogging.heart.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.exception.BoardNotFoundException;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.heart.domain.Heart;
import com.multi.blogging.multiblogging.heart.exception.HeartConflictException;
import com.multi.blogging.multiblogging.heart.exception.HeartNotFoundException;
import com.multi.blogging.multiblogging.heart.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void insert(String memberEmail, Long boardId) {
        Member member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        if (heartRepository.findByMemberAndBoard(member.getId(), boardId).isPresent()) {
            throw new HeartConflictException();
        }

        heartRepository.save(Heart.builder().member(member).board(board).build());
    }

    @Transactional
    public void delete(String memberEmail, Long boardId) {
        Member member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        Heart heart = heartRepository.findByMemberAndBoard(member.getId(), boardId).orElseThrow(HeartNotFoundException::new);
        heartRepository.delete(heart);
    }

    @Transactional(readOnly = true)
    public int getHearts(Long boardId){
        return heartRepository.getCountByBoard(boardId);
    }
}
