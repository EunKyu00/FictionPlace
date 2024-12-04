package com.example.fiction_place1.domain.comment.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.comment.service.CommentService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/create/{id}")
    public String createComment(RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long id,
                                @RequestParam(value="content") String content,
                                Model model, HttpSession session){

        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorContent", "내용을 입력해주세요.");
            return String.format("redirect:/board/detail/%s", id); // 리디렉션 전에 에러 메시지 처리
        }

        // 로그인된 사용자 세션 받아오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser != null) {
            model.addAttribute("nickname",siteUser.getNickname());
        } else {
            model.addAttribute("message", "로그인 후 이용해주세요.");
        }

        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");
        if (companyUser != null) {
            model.addAttribute("companyName", companyUser.getCompanyName());
        }else {
            model.addAttribute("message","로그인 후 이용해주세요.");
        }

        Board board = this.boardService.getBoard(id);
        if (siteUser != null){
            commentService.createComment(board,content,siteUser);
        }else if (companyUser != null){
            commentService.createComment(board,content,companyUser);
        }
        return String.format("redirect:/board/detail/%s", id);
    }
}
