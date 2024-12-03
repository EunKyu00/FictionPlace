package com.example.fiction_place1.domain.comment.repository;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBoard(Board board);  // 게시글에 해당하는 댓글 목록을 조회
}
