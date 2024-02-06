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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final HeartRepository heartRepository;
//    private final String HEART_PREFIX = "Heart ";

    public void insert(String memberEmail, Long boardId) {
        Member member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        var optionalHeart = heartRepository.findByMemberAndBoard(member.getId(), boardId);
        if (optionalHeart.isEmpty()) {
            Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
            board.setLikeCount(board.getLikeCount() + 1);
            heartRepository.save(new Heart(member, board));
        }
    }

    public void delete(String memberEmail, Long boardId) {
        Member member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        var optionalHeart = heartRepository.findByMemberAndBoard(member.getId(), boardId);

        board.setLikeCount(board.getLikeCount() - 1);
        optionalHeart.ifPresent(heartRepository::delete);
    }

    public List<Heart> getHearts(Long boardId) {
        return heartRepository.findAllByBoardId(boardId);

    }
}

//    public void transferHeartsToDb(){
//        Map<String, Set> boardIdAndAddressMap = redisService.getKeyAndSetOpsContainPrefix(HEART_PREFIX);
//        for (String key : boardIdAndAddressMap.keySet()) {
//            Set IPAddresses = boardIdAndAddressMap.get(key);
//            Long boardId = Long.valueOf(key.replace(VIEW_COUNT_PREFIX, ""));
//            Optional<Board> board=boardRepository.findById(boardId);
//            if (board.isPresent()){
//                int oldViewCount = board.get().getViewCount();
//                int newViewCount = oldViewCount + IPAddresses.size();
//                board.get().setViewCount(newViewCount);
//            }
//        }
//    }

