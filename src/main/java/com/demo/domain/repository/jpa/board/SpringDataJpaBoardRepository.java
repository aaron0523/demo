package com.demo.domain.repository.jpa.board;

import com.demo.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaBoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByAuthorId(Long authorId);
}
