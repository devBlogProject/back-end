package com.multi.blogging.multiblogging.heart.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.config.QueryDslTestConfig;
import com.multi.blogging.multiblogging.heart.domain.Heart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static com.multi.blogging.multiblogging.Constant.TEST_EMAIL;
import static com.multi.blogging.multiblogging.Constant.TEST_NICK;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import(QueryDslTestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HeartRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    HeartRepository heartRepository;

    Member testMember;
    Board testBoard;

    @BeforeEach
    void setUp() {
        testMember=memberRepository.save(Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build());
        testBoard=boardRepository.save(Board.builder().title("title").postNumber(1).build());
    }

    @Test
    void 좋아요개수(){
        for (int i=0;i<5;i++){
            heartRepository.save(Heart.builder().board(testBoard).build());
        }
        Board testBoard2 = Board.builder().title("title2").postNumber(2).build();
        boardRepository.save(testBoard2);

        for (int i=0;i<3;i++){
            heartRepository.save(Heart.builder().board(testBoard2).build());
        }

        assertEquals(5,heartRepository.getCountByBoard(testBoard.getId()));
        assertEquals(3,heartRepository.getCountByBoard(testBoard2.getId()));
    }

    @Test
    void findByMemberAndBoard() {
        heartRepository.save(Heart.builder().member(testMember).board(testBoard).build());
        assertTrue(heartRepository.findByMemberAndBoard(testMember.getId(), testBoard.getId()).isPresent());
    }
}