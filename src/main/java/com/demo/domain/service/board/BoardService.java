package com.demo.domain.service.board;

import com.demo.domain.board.Board;
import com.demo.util.UploadFile;
import com.demo.web.dto.board.BoardCreatedDto;
import com.demo.web.dto.board.BoardDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    Page<BoardDto> getAllBoards(Pageable pageable);
    Optional<Board> getBoardById(Long boardId);
    List<Board> getBoardsByAuthorId(Long authorId);
    Board createBoard(BoardCreatedDto createDto, Long authorId, List<UploadFile> uploadFiles);
    void updateBoard(Long boardId, BoardUpdatedDto updateDto, List<UploadFile> uploadFiles);
    void deleteBoard(Long boardId);
}
