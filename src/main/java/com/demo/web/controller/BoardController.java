package com.demo.web.controller;

import com.demo.domain.board.Board;
import com.demo.domain.board.BoardType;
import com.demo.domain.member.Member;
import com.demo.domain.service.board.BoardService;
import com.demo.util.FileStore;
import com.demo.util.UploadFile;
import com.demo.web.SessionConst;
import com.demo.web.dto.board.BoardCreatedDto;
import com.demo.web.dto.board.BoardDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private static final int PAGE_SIZE = 10;

    private final BoardService boardService;
    private final FileStore fileStore;

    @GetMapping("/board")
    public String getAllBoards(Model model, @RequestParam(defaultValue = "0") int startPage) {
        Pageable pageable = PageRequest.of(startPage, PAGE_SIZE, Sort.by("id").descending());
        Page<BoardDto> boardPage = boardService.getAllBoards(pageable);

        int currPage = boardPage.getNumber() + 1; // 현재 페이지 번호
        long totalPostCnt = boardPage.getTotalElements(); // 총 게시글 수
        int lastPage = boardPage.getTotalPages(); // 마지막 페이지 번호

        int prePage = Math.max(currPage - 1, 1); // 이전 페이지 번호
        boolean hasPrePage = currPage > 1; // 이전 페이지 유무

        model.addAttribute("boardList", boardPage.getContent());
        model.addAttribute("currPage", currPage);
        model.addAttribute("totalPostCnt", totalPostCnt);
        model.addAttribute("prePage", prePage);
        model.addAttribute("hasPrePage", hasPrePage);
        model.addAttribute("lastPage", lastPage);

        return "community/board";
    }


    @GetMapping("/detail/{id}")
    public String getBoardById(@PathVariable("id") Long id, Model model) {
        Optional<Board> board = boardService.getBoardById(id);
        if (board.isPresent()) {
            model.addAttribute("board", board.get());
            return "community/detailBoardForm";
        } else {
            return "error";
        }
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        String currentUser = loggedInMember.getNickName();
        model.addAttribute("createDto", new BoardCreatedDto());
        model.addAttribute("currentUser", currentUser);

        return "community/addBoardForm";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute BoardCreatedDto createDto, @RequestParam("authorId") Long authorId,
                       @RequestParam("files") List<MultipartFile> files) {
        try {
            List<UploadFile> uploadFiles = fileStore.storeFiles(files, BoardType.valueOf(createDto.getBoardType()));
            boardService.createBoard(createDto, authorId, uploadFiles);
        } catch (IOException e) {
            log.error("게시물 저장 중 오류가 발생했습니다.", e);
            return "error";
        }

        return "redirect:/boards/board";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<Board> board = boardService.getBoardById(id);
        if (board.isPresent()) {
            model.addAttribute("board", board.get());
            return "community/editBoardForm";
        } else {
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute BoardUpdatedDto updateDto,
                         @RequestParam("files") List<MultipartFile> files) {
        try {
            List<UploadFile> uploadFiles = fileStore.storeFiles(files, BoardType.valueOf(updateDto.getBoardType()));
            boardService.updateBoard(id, updateDto, uploadFiles);
        } catch (IOException e) {
            log.error("게시물 업데이트 중 오류가 발생했습니다.", e);
            return "error";
        }
        return "redirect:/boards/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/boards/board";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
