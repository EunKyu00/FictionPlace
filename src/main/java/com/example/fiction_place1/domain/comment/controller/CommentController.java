package com.example.fiction_place1.domain.comment.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.comment.service.CommentService;
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
                                @RequestParam(value="content") String content){
        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorContent", "내용을 입력해주세요.");
            return String.format("redirect:/board/detail/%s", id); // 리디렉션 전에 에러 메시지 처리
        }
        Board board = this.boardService.getBoard(id);
        this.commentService.createComment(board,content);
        return String.format("redirect:/board/detail/%s", id);
    }
}
