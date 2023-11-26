package com.multi.blogging.multiblogging.board.repository.custom;

import com.multi.blogging.multiblogging.board.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface CustomBoardRepository {
    Optional<Board> findByIdWithCategory(Long id);

    Optional<Board> findByIdWithCategoryAndMember(Long id);

    Optional<Board> findByIdWithMember(Long id);

    Slice<Board> findSliceWithMember(Pageable pageable);
}
