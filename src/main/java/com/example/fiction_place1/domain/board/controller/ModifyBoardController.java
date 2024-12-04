package com.example.fiction_place1.domain.board.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.form.BoardForm;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.board_type.service.BoardTypeService;
import com.example.fiction_place1.domain.comment.service.CommentService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ModifyBoardController {
    private final BoardService boardService;

    //게시글 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/modify/{id}")
    public String modifyBoardForm(@PathVariable("id") Long id, Model model) {
        Board board = boardService.getBoard(id);

        BoardForm boardForm = new BoardForm();
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());

        model.addAttribute("boardForm", boardForm);
        model.addAttribute("boardId", id);

        return "modify_board";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/modify/{id}")
    public String modifyBoard(@Valid BoardForm boardForm, BindingResult bindingResult,
                              @PathVariable("id") Long id,
                              HttpSession session) {
        Board board = boardService.getBoard(id);

        // 세션에서 로그인된 사용자 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 로그인한 사용자가 게시글의 작성자인지 확인
        if (siteUser != null && board.getSiteUser().getId().equals(siteUser.getId())) {
            boardService.modify(board, boardForm.getTitle(), boardForm.getContent());
        } else if (companyUser != null && board.getCompanyUser().getId().equals(companyUser.getId())) {
            boardService.modify(board, boardForm.getTitle(), boardForm.getContent());
        }

        // 게시글 수정 후, 게시글의 boardTypeId를 가져와서 해당 타입의 게시판 목록으로 리다이렉트
        Long boardTypeId = board.getBoardType().getId();  // 게시글의 boardTypeId 가져오기
        return String.format("redirect:/board?boardTypeId=%d", boardTypeId); // boardTypeId로 리다이렉트
    }
}
