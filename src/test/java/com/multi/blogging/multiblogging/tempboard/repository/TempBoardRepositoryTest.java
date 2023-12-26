package com.multi.blogging.multiblogging.tempboard.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.config.QueryDslTestConfig;
import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import(QueryDslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TempBoardRepositoryTest {

    @Autowired
    TempBoardRepository tempBoardRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByAuthorEmail(){
        Member member = Member.builder().email(TEST_EMAIL).password(TEST_PASSWORD).nickName(TEST_NICK).build();
        memberRepository.save(member);
        TempBoard tempBoard = new TempBoard();
        tempBoard.setTitle("title");
        tempBoard.setContent("content");
        tempBoard.changeAuthor(member);

        tempBoardRepository.save(tempBoard);
        assertTrue(tempBoardRepository.findByAuthorEmail(TEST_EMAIL).isPresent());
    }
}