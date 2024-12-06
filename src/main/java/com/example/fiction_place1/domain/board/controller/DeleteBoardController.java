package com.example.fiction_place1.domain.board.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DeleteBoardController {
    private final BoardService boardService;

    //게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        Board board = this.boardService.getBoard(id);
        this.boardService.delete(board);
        Long boardTypeId = board.getBoardType().getId();
        return String.format("redirect:/board?boardTypeId=%d", boardTypeId);
    }
}
