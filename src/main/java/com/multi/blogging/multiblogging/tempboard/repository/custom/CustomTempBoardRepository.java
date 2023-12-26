package com.multi.blogging.multiblogging.tempboard.repository.custom;

import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;

import java.util.Optional;

public interface CustomTempBoardRepository {
    Optional<TempBoard> findByAuthorEmail(String email);
}
