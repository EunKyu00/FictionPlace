package com.example.fiction_place1.domain.comment.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getCommentsByBoard(Board board) {
        return commentRepository.findByBoard(board);  // 게시글에 해당하는 댓글을 반환
    }

    public void createComment(Board board,String content){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBoard(board);
        this.commentRepository.save(comment);
    }
}
