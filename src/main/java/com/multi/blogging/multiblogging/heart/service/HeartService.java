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
import com.multi.blogging.multiblogging.infra.redisDb.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final RedisService redisService;

    private final String HEART_PREFIX = "HEART ";

    public void insert(String memberEmail, Long boardId) {
        redisService.setSetOps(HEART_PREFIX+boardId,memberEmail);
    }

    public void delete(String memberEmail, Long boardId) {
        redisService.deleteSetOps(HEART_PREFIX+boardId,memberEmail);
    }
    public Set<Object> getHearts(Long boardId){
        return redisService.getSetOps(HEART_PREFIX + boardId);
    }
}
