package com.multi.blogging.multiblogging.tempboard.service;

import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import com.multi.blogging.multiblogging.tempboard.exception.TempBoardNotFoundException;
import com.multi.blogging.multiblogging.tempboard.repository.TempBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        exTempBoard.ifPresent(tempBoardRepository::delete);

        TempBoard newTempBoard = new TempBoard();
        newTempBoard.setTitle(title);
        newTempBoard.setContent(content);
        newTempBoard.changeAuthor(author);

        return tempBoardRepository.save(newTempBoard);
    }

    @Transactional
    public void deleteTempBoard(String authorEmail) {
        var tempBoard = tempBoardRepository.findByAuthorEmail(authorEmail);
        tempBoard.ifPresent(tempBoardRepository::delete);
    }
}
