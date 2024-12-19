package com.example.fiction_place1.domain.comment.repository;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBoard(Board board);// 게시글에 해당하는 댓글 목록을 조회
    List<Comment> findByWebtoonEpisode(WebToonEpisode webToonEpisode);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.board.id = :boardId")
    long countCommentsByBoardId(@Param("boardId") Long boardId);

    int countByBoardId(Long boardId); //댓글갯수찾아오는레포

    List<Comment> findByBoardIdOrderByCreatedDateDesc(Long boardId); //댓글 최신순으로가져오기
}
