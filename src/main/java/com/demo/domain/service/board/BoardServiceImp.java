package com.demo.domain.service.board;

import com.demo.domain.board.BoardType;
import com.demo.domain.member.Member;
import com.demo.domain.board.Board;
import com.demo.domain.repository.support.MemberRepository;
import com.demo.domain.repository.support.BoardRepository;
import com.demo.util.UploadFile;
import com.demo.web.dto.board.BoardCreatedDto;
import com.demo.web.dto.board.BoardDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImp implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardServiceImp(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    public Page<BoardDto> getAllBoards(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(board -> convertToBoardDto(board, pageable));
    }

    public Optional<Board> getBoardById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public List<Board> getBoardsByAuthorId(Long authorId) {
        return boardRepository.findByAuthorId(authorId);
    }

    public Board createBoard(BoardCreatedDto createDto, Long authorId, List<UploadFile> uploadFiles) {
        Member author = findMemberById(authorId);
        Board board = new Board();
        board.setAuthor(author);
        board.setTitle(createDto.getTitle());
        board.setContent(createDto.getContent());
        board.setYoutubeUrl(createDto.getYoutubeUrl());
        board.setBoardType(BoardType.valueOf(createDto.getBoardType()));
        board.setUploadFiles(uploadFiles);
        return boardRepository.save(board);
    }

    public void updateBoard(Long boardId, BoardUpdatedDto updateDto, List<UploadFile> uploadFiles) {
        Board board = findBoardById(boardId);
        board.setTitle(updateDto.getTitle());
        board.setContent(updateDto.getContent());
        board.setYoutubeUrl(updateDto.getYoutubeUrl());
        board.setBoardType(BoardType.valueOf(updateDto.getBoardType()));
        board.setUploadFiles(uploadFiles);
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId) {
        Board board = findBoardById(boardId);
        boardRepository.delete(board);
    }

    private Member findMemberById(Long authorId) {
        return memberRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 작성자 ID입니다: " + authorId));
    }

    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 ID입니다: " + boardId));
    }

    private BoardDto convertToBoardDto(Board board, Pageable pageable) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardPage(boardRepository.findAll(pageable));
        return boardDto;
    }
}
