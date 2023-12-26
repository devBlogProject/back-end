package com.multi.blogging.multiblogging.tempboard.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.board.service.BoardService;
import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import com.multi.blogging.multiblogging.tempboard.repository.TempBoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.multi.blogging.multiblogging.Constant.TEST_EMAIL;
import static com.multi.blogging.multiblogging.Constant.TEST_NICK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TempBoardService.class})
@ActiveProfiles("test")
class TempBoardServiceTest {

    @Autowired
    TempBoardService tempBoardService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    TempBoardRepository tempBoardRepository;

    @Test
    void saveTempBoard(){
        Member testMember = Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build();
        TempBoard exTempBoard = new TempBoard();
        exTempBoard.setTitle("title");
        exTempBoard.setContent("content");
        exTempBoard.changeAuthor(testMember);

        given(memberRepository.findOneByEmail(anyString())).willReturn(Optional.of(testMember));
        given(tempBoardRepository.findByAuthorEmail(anyString())).willReturn(Optional.of(exTempBoard));

        tempBoardService.saveTempBoard(TEST_EMAIL, "title", "content");

        verify(tempBoardRepository,times(1)).delete(exTempBoard);
        verify(tempBoardRepository, times(1)).save(any());
        verify(tempBoardRepository, never()).save(exTempBoard);
    }

    @Test
    void deleteTempBoard(){
        Member testMember = Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build();
        TempBoard tempBoard = new TempBoard();
        tempBoard.setTitle("title");
        tempBoard.setContent("content");
        tempBoard.changeAuthor(testMember);

        given(tempBoardRepository.findByAuthorEmail(anyString())).willReturn(Optional.of(tempBoard));

        tempBoardService.deleteTempBoard(TEST_EMAIL);

        verify(tempBoardRepository,times(1)).delete(tempBoard);
    }
}