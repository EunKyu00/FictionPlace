package com.example.fiction_place1.domain.comment.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getCommentsByBoard(Board board) {
        return commentRepository.findByBoard(board);  // 게시글에 해당하는 댓글을 반환
    }
    public List<Comment> getCommentByEpisode(WebToonEpisode webToonEpisode){
        return commentRepository.findByWebtoonEpisode(webToonEpisode);
    }

    public void createBoardComment(Board board, String content, User user){
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

    public void createEpisodeComment(WebToonEpisode webToonEpisode,String content, User user){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setWebtoonEpisode(webToonEpisode);

        if (user instanceof  SiteUser){
            SiteUser siteUser = (SiteUser) user;
            comment.setSiteUser(siteUser);
        }else if (user instanceof CompanyUser){
            CompanyUser companyUser = (CompanyUser) user;
            comment.setCompanyUser(companyUser);
        }
        this.commentRepository.save(comment);
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }

    public void delete(Comment comment){
        this.commentRepository.delete(comment);
    }
}
