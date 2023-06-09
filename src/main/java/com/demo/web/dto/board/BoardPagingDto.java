package com.demo.web.dto.board;

import com.demo.domain.board.Board;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class BoardPagingDto {
    private Page<Board> boardPage;
}
