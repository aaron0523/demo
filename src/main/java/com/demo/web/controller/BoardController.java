package com.demo.web.controller;

import com.demo.domain.board.Board;
import com.demo.domain.board.BoardType;
import com.demo.domain.member.Member;
import com.demo.domain.service.board.BoardService;
import com.demo.util.FileStore;
import com.demo.util.UploadFile;
import com.demo.web.SessionConst;
import com.demo.web.dto.board.BoardCreatedDto;
import com.demo.web.dto.board.BoardUpdatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class BoardController {

    private static final int PAGE_SIZE = 10;

    private final BoardService boardService;
    private final FileStore fileStore;

    @GetMapping("/board")
    public String getAllBoards(Model model,
                               @RequestParam(value = "boardType", defaultValue = "COMMUNITY") String boardType,
                               @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<Board> boardPage = boardService.getBoardsByType(pageable, boardType);

        model.addAttribute("boardPage", boardPage);

        return "community/board";
    }

    @GetMapping("/detail/{id}")
    public String getBoardById(@PathVariable("id") Long id, Model model) {
        Optional<Board> board = boardService.getBoardById(id);
        if (board.isPresent()) {
            Board boardData = board.get();

            model.addAttribute("board", boardData);
            model.addAttribute("boardType", boardData.getBoardType().name());

            return "community/detailBoardForm";
        } else {
            return "error";
        }
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpSession session, HttpServletResponse response) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Long currentUserId = loggedInMember.getId();
        String currentUserNickname = loggedInMember.getNickName();

        model.addAttribute("createDto", new BoardCreatedDto());
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("currentUserNickname", currentUserNickname);

        // 헤더에 캐시 제어 설정 추가
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");

        return "community/addBoardForm";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute BoardCreatedDto createDto,
                       @RequestParam("id") Long id,
                       @RequestParam("files") List<MultipartFile> files,
                       @RequestParam("boardType") String boardType) {
        try {
            BoardType type = BoardType.valueOf(boardType);
            List<UploadFile> uploadFiles = fileStore.storeFiles(files, type);
            boardService.createBoard(createDto, id, uploadFiles);
        } catch (IOException e) {
            log.error("게시물 저장 중 오류가 발생했습니다.", e);
            return "error";
        }

        return "redirect:/community/board";
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

    public String update(@PathVariable("id") Long id, @ModelAttribute BoardUpdatedDto updateDto,
                         @RequestParam("files") List<MultipartFile> files,
                         @RequestParam(value = "filesToDelete", required = false) List<String> filesToDelete) {
        try {
            List<UploadFile> uploadFiles = fileStore.storeFiles(files, BoardType.valueOf(updateDto.getBoardType()));
            boardService.updateBoard(id, updateDto, uploadFiles, filesToDelete);
        } catch (IOException e) {
            log.error("게시물 업데이트 중 오류가 발생했습니다.", e);
            return "error";
        }
        return "redirect:/community/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/community/board";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
