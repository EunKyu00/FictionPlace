package com.example.fiction_place1.domain.board.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.form.BoardForm;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.service.BoardTypeService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FreeBoardController {
    private final BoardService boardService;
    private final BoardTypeService boardTypeService;

    // 게시판 목록 페이지
    @GetMapping("/board")
    public String boardList(@RequestParam(value = "boardTypeId", defaultValue = "1") Long boardTypeId, Model model) {

        // boardTypeId에 맞는 게시글 목록
        List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
        model.addAttribute("boardTypes", boardTypes);

        List<Board> boardList = this.boardService.getBoardType(boardTypeId);
        model.addAttribute("boardList", boardList);

        // boardTypeId에 맞는 게시판 이름을 모델에 추가
        BoardType selectedBoardType = boardTypeService.findById(boardTypeId);
        model.addAttribute("selectedBoardType", selectedBoardType);

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

        //로그인별 세션 받아옴
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
            model.addAttribute("boardTypes", boardTypes);
            model.addAttribute("boardForm", boardForm);
            return "create_board";
        }

        // 일반회원 또는 기업회원 게시글 작성
        if (siteUser != null) {
            boardService.createFreeBoard(boardForm.getTitle(), boardForm.getContent(), boardTypeId, siteUser);
        } else if (companyUser != null) {
            boardService.createFreeBoard(boardForm.getTitle(), boardForm.getContent(), boardTypeId, companyUser);
        }
        return "redirect:/board?boardTypeId=" + boardTypeId;
    }

}


