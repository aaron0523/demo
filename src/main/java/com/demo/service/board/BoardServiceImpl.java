package com.demo.service.board;

import com.demo.domain.board.BoardType;
import com.demo.domain.member.Member;
import com.demo.domain.board.Board;
import com.demo.repository.support.MemberRepository;
import com.demo.repository.support.BoardRepository;
import com.demo.util.UploadFile;
import com.demo.web.dto.board.BoardCreatedDto;
import com.demo.web.dto.board.BoardPagingDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardServiceImpl(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    public Page<Board> getBoardsByType(Pageable pageable, String boardType) {
        BoardType type = BoardType.valueOf(boardType);
        return boardRepository.getBoardsByType(type, pageable);
    }

    public Optional<Board> getBoardById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public List<Board> getBoardsByAuthorId(Long authorId) {
        return boardRepository.findByAuthorId(authorId);
    }

    public Board createBoard(BoardCreatedDto createDto, Long id, List<UploadFile> uploadFiles) {

        // 게시글 존재 여부 확인
        validateExistingBoard(createDto.getId());

        Member author = findMemberById(id);
        Board board = new Board();
        board.setAuthor(author);
        board.setTitle(createDto.getTitle());
        board.setContent(createDto.getContent());
        board.setYoutubeUrl(createDto.getYoutubeUrl());
        board.setBoardType(BoardType.valueOf(createDto.getBoardType()));
        for (UploadFile uploadFile : uploadFiles) {
            board.addUploadFile(uploadFile);
        }
        return boardRepository.save(board);
    }

    public void updateBoard(Long boardId, BoardUpdatedDto updateDto, List<UploadFile> uploadFiles, List<String> filesToDelete) {
        Board board = findBoardById(boardId);
        board.setTitle(updateDto.getTitle());
        board.setContent(updateDto.getContent());
        board.setYoutubeUrl(updateDto.getYoutubeUrl());
        board.setBoardType(BoardType.valueOf(updateDto.getBoardType()));
        board.setUploadFiles(uploadFiles);

        // 업로드되어 있는 파일 삭제
        if (filesToDelete != null && !filesToDelete.isEmpty()) {
            List<Long> fileIdsToDelete = new ArrayList<>();
            for (String storeFileName : filesToDelete) {
                Optional<UploadFile> optionalUploadFile = Optional.ofNullable(board.findUploadFileByStoreFileName(storeFileName));
                if (optionalUploadFile.isPresent()) {
                    UploadFile uploadFile = optionalUploadFile.get();
                    fileIdsToDelete.add(uploadFile.getId());
                }
            }
            board.removeUploadFilesByIds(fileIdsToDelete);
        }

        boardRepository.save(board);
    }


    public void deleteBoard(Long boardId) {
        Board board = findBoardById(boardId);
        boardRepository.delete(board);
    }

    private Member findMemberById(Long authorId) {
        return memberRepository.findById(authorId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 작성자 ID입니다: " + authorId));
    }

    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 게시글 ID입니다: " + boardId));
    }

    private BoardPagingDto convertToBoardDto(Board board) {
        BoardPagingDto boardPagingDto = new BoardPagingDto();
        // Collections.singletonList(board) : board 객체를 요소로 갖는 길이 1인 리스트를 생성
        // new PageImpl<>() : 형변환을 위해 사용
        Page<Board> boardPage = new PageImpl<>(Collections.singletonList(board));
        boardPagingDto.setBoardPage(boardPage);
        return boardPagingDto;
    }

    private void validateExistingBoard(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new IllegalStateException("존재하지 않는 게시글입니다. 게시글 ID: " + boardId);
        }
    }
}
