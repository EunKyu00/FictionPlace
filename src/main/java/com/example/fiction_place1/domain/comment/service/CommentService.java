package com.example.fiction_place1.domain.comment.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
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

    public void createComment(Board board, String content, User user){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBoard(board);
        //댓글 작성자 설정
        if (user instanceof SiteUser) {
            SiteUser siteUser = (SiteUser) user;
            comment.setSiteUser(siteUser);
        } else if (user instanceof CompanyUser) {
            CompanyUser companyUser = (CompanyUser) user;
            comment.setCompanyUser(companyUser);
        }
        this.commentRepository.save(comment);
    }
}
