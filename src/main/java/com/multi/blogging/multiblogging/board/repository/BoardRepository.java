package com.multi.blogging.multiblogging.board.repository;

import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.repository.custom.CustomBoardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>, CustomBoardRepository {

    Slice<Board> findSliceBy(Pageable pageable);
}
