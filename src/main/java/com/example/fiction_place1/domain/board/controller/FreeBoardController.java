package com.example.fiction_place1.domain.board.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.form.BoardForm;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.service.BoardTypeService;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.service.CommentService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FreeBoardController {
    private final BoardService boardService;
    private final BoardTypeService boardTypeService;
    private final CommentService commentService;

    // 게시판 목록 페이지
    @GetMapping("/board")
    public String boardList(
            @RequestParam(value = "boardTypeId", defaultValue = "1") Long boardTypeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // 모든 게시판 타입 목록 가져오기
        List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
        model.addAttribute("boardTypes", boardTypes);

        // 선택한 게시판 타입 가져오기
        BoardType selectedBoardType = boardTypeService.findById(boardTypeId);
        model.addAttribute("selectedBoardType", selectedBoardType);

        // 페이징 처리된 게시글 목록 가져오기
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardPage = boardService.getBoardType(boardTypeId, pageable);

        // 게시글 목록 및 페이징 정보 모델에 추가
        model.addAttribute("boardList", boardPage.getContent()); // 게시글 리스트
        model.addAttribute("currentPage", boardPage.getNumber()); // 현재 페이지
        model.addAttribute("size", boardPage.getSize()); // 페이지 크기
        model.addAttribute("totalPages", boardPage.getTotalPages()); // 전체 페이지 수

        // 선택한 게시판 타입 모델에 추가
        model.addAttribute("boardTypeId", boardTypeId);

        return "board_list";
    }

    // 게시글 작성 페이지
    @GetMapping("/board/create")
    public String freeBoardCreate(BoardForm boardForm, Model model) {
        List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
        model.addAttribute("boardTypes", boardTypes);
        return "create_board";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/create")
    public String createFreeBoard(@Valid BoardForm boardForm, BindingResult bindingResult,
                                  HttpSession session,
                                  @RequestParam("boardTypeId") Long boardTypeId,
                                  Model model) {

        model.addAttribute("boardTypeId", boardTypeId);

        // 로그인된 사용자 세션 받아오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 로그인 체크
        if (siteUser == null && companyUser == null) {
            model.addAttribute("errorMessage", "로그인 후 이용해주세요.");
            List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
            model.addAttribute("boardTypes", boardTypes);
            return "create_board"; // 게시글 작성 페이지로 다시 이동
        }

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
            model.addAttribute("boardTypes", boardTypes);
            model.addAttribute("boardForm", boardForm);
            return "create_board";
        }

        // 일반회원 또는 기업회원 게시글 작성
        if (siteUser != null) {
            model.addAttribute("nickname", siteUser.getNickname());
            boardService.createFreeBoard(boardForm.getTitle(), boardForm.getContent(), boardTypeId, siteUser);
        } else if (companyUser != null) {
            model.addAttribute("companyName", companyUser.getCompanyName());
            boardService.createFreeBoard(boardForm.getTitle(), boardForm.getContent(), boardTypeId, companyUser);
        }

        return "redirect:/board?boardTypeId=" + boardTypeId;
    }

    //게시글 상세
    @GetMapping("/board/detail/{id}")
    public String boardDetail(Model model, @PathVariable("id") Long id){
        Board board = boardService.getBoard(id);
        List<Comment> comments = commentService.getCommentsByBoard(board);
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        return "board_detail";
    }
}


