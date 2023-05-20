package com.demo.domain.repository.jpa.board;

import com.demo.domain.board.Board;
import com.demo.domain.repository.support.BoardRepository;
import com.demo.domain.repository.support.QuerydslRepositorySupport;
import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.demo.domain.board.QBoard.board;

@Repository
public class JpaBoardRepository extends QuerydslRepositorySupport implements BoardRepository {

    private final SpringDataJpaBoardRepository jpaBoardRepository;

    public JpaBoardRepository(SpringDataJpaBoardRepository jpaBoardRepository) {
        super(Board.class);
        this.jpaBoardRepository = jpaBoardRepository;
    }

    public Page<Board> findAll(Pageable pageable) {
        return applyPagination(pageable, query -> selectFrom(board));
    }

    public Optional<Board> findById(Long id) {
        return jpaBoardRepository.findById(id);
    }

    public List<Board> findByAuthorId(Long authorId) {
        return jpaBoardRepository.findByAuthorId(authorId);
    }

    public Board save(Board board) {
        return jpaBoardRepository.save(board);
    }

    public void updateBoard(Long boardId, BoardUpdatedDto boardUpdateDto) {
        getQueryFactory().update(board)
                .set(board.title, boardUpdateDto.getTitle())
                .set(board.content, boardUpdateDto.getContent())
                .set(board.youtubeUrl, boardUpdateDto.getYoutubeUrl())
                .where(board.id.eq(boardId))
                .execute();
    }

    public void delete(Board board) {
        jpaBoardRepository.delete(board);
    }

    public long count() {
        return jpaBoardRepository.count();
    }
}
