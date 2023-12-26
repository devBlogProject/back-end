package com.multi.blogging.multiblogging.tempboard.repository;

import com.multi.blogging.multiblogging.tempboard.domain.TempBoard;
import com.multi.blogging.multiblogging.tempboard.repository.custom.CustomTempBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempBoardRepository extends JpaRepository<TempBoard,Long>, CustomTempBoardRepository {
}
