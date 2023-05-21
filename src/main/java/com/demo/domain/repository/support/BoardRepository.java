package com.demo.domain.repository.support;

import com.demo.domain.board.Board;
import com.demo.domain.board.BoardType;
import com.demo.web.dto.board.BoardPagingDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Page<Board> findAll(Pageable pageable);
    Optional<Board> findById(Long id);
    List<Board> findByAuthorId(Long authorId);
    Board save(Board board);
    void updateBoard(Long boardId, BoardUpdatedDto boardUpdateDto);
    void delete(Board board);
    Page<Board> getBoardsByType(BoardType boardType, Pageable pageable);
}
