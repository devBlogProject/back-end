package com.multi.blogging.multiblogging.tempboard.service;

import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import com.multi.blogging.multiblogging.tempboard.exception.TempBoardNotFoundException;
import com.multi.blogging.multiblogging.tempboard.repository.TempBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TempBoardService {
    private final TempBoardRepository tempBoardRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public TempBoard getTempBoard(String authorEmail) {
        return tempBoardRepository.findByAuthorEmail(authorEmail).orElseThrow(TempBoardNotFoundException::new);
    }

    @Transactional
    public TempBoard saveTempBoard(String authorEmail, String title, String content) {
        var author = memberRepository.findOneByEmail(authorEmail).orElseThrow(MemberNotFoundException::new);
        var exTempBoard = tempBoardRepository.findByAuthorEmail(authorEmail);
        if(exTempBoard.isPresent()){
            exTempBoard.get().setTitle(title);
            exTempBoard.get().setContent(content);
            return exTempBoard.get();
        }else{
            TempBoard tempBoard = new TempBoard();
            tempBoard.setTitle(title);
            tempBoard.setContent(content);
            tempBoard.changeAuthor(author);
            return tempBoardRepository.save(tempBoard);
        }
    }

    @Transactional
    public void deleteTempBoard(String authorEmail) {
        var tempBoard = tempBoardRepository.findByAuthorEmail(authorEmail);
        tempBoard.ifPresent(tempBoardRepository::delete);
    }
}
