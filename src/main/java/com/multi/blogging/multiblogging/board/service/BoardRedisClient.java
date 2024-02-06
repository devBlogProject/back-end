package com.multi.blogging.multiblogging.board.service;

import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.infra.redisDb.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
class BoardRedisClient {
    private final RedisService redisService;
    static final String VIEW_COUNT_PREFIX = "Viewcount ";

    private String getKey(Board board) {
        return VIEW_COUNT_PREFIX + board.getId();
    }

    public void addClientIpToCache(String ipAddress, Board board) {

        redisService.setSetOps(getKey(board), ipAddress);
        redisService.setExpireDays(getKey(board),1); // 유효기간은 하루 = 즉 다음날에 같은 ip로 조회수를 올릴 수 있다.
    }

    public Set<Object> getClientIpAddressOfBoard(Board board) {
        return redisService.getSetOps(getKey(board));
    }

}
