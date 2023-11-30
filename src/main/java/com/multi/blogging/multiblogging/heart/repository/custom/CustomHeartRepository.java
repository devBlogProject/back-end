package com.multi.blogging.multiblogging.heart.repository.custom;

import com.multi.blogging.multiblogging.heart.domain.Heart;

import java.util.Optional;

public interface CustomHeartRepository {
    Optional<Heart> findByMemberAndBoard(Long memberId, Long boardId);

    Optional<Heart> findByMemberAndBoardWithMember(Long memberId, Long boardId);
}
