package com.demo.service.board;

import com.demo.domain.board.Board;
import com.demo.util.UploadFile;
import com.demo.web.dto.board.BoardCreatedDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    Page<Board> getBoardsByType(Pageable pageable, String boardType);
    Optional<Board> getBoardById(Long boardId);
    List<Board> getBoardsByAuthorId(Long authorId);
    Board createBoard(BoardCreatedDto createDto, Long authorId, List<UploadFile> uploadFiles);
    void updateBoard(Long boardId, BoardUpdatedDto updateDto, List<UploadFile> uploadFiles, List<String> filesToDelete);
    void deleteBoard(Long boardId);
}
