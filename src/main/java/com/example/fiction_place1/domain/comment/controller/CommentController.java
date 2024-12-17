package com.example.fiction_place1.domain.comment.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.comment.service.CommentService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.service.WebToonEpisodeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final BoardService boardService;
    private final WebToonEpisodeService webToonEpisodeService;
    private final CommentService commentService;

    //게시판 댓글 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/create/{id}")
    public String createComment(RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long id,
                                @RequestParam(value="content") String content,
                                Model model, HttpSession session) {
        // 댓글 내용이 비었을 때 에러 처리
        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorContent", "내용을 입력해주세요.");
            return String.format("redirect:/board/detail/%s", id); // 리디렉션 전에 에러 메시지 처리
        }

        // 로그인된 사용자 세션 받아오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 로그인되지 않은 경우 리디렉션 처리
        if (siteUser == null && companyUser == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 후 이용해주세요.");
            return String.format("redirect:/board/detail/%s", id);  // 로그인되지 않은 경우 리디렉션
        }

        // 로그인된 됐으면 댓글등록
        Board board = this.boardService.getBoard(id);
        if (siteUser != null) {
            commentService.createBoardComment(board, content, siteUser);
        } else if (companyUser != null) {
            commentService.createBoardComment(board, content, companyUser);
        }
        return String.format("redirect:/board/detail/%s", id);
    }
    //웹툰 에피소드 댓글 등록
    @PostMapping("/episode/comment/create/{id}")
    public String episodeCommentCreate(@PathVariable("id") Long episodeId,
                                       @RequestParam("content") String content,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session){

        WebToonEpisode webToonEpisode = this.webToonEpisodeService.findById(episodeId);

        if (content == null || content.trim().isEmpty()){
            redirectAttributes.addFlashAttribute("errorContent", "내용을 입력해주세요.");
            return String.format("redirect:/webtoon/episode/%s", episodeId);
        }

        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser)  session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null) {
            redirectAttributes.addFlashAttribute("message","로그인 후 이용해주세요.");
            return String.format("redirect:/webtoon/episode/%s", episodeId);
        }

        if (siteUser != null){
            commentService.createEpisodeComment(webToonEpisode,content,siteUser);
        }else if (companyUser != null){
            commentService.createEpisodeComment(webToonEpisode,content,companyUser);
        }
        return String.format("redirect:/webtoon/episode/%s",episodeId);
    }


    //게시판 댓글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        this.commentService.delete(comment);
        Long boardId = comment.getBoard().getId();
        return String.format("redirect:/board/detail/%d", boardId);
    }

    //에피소드 댓글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/episode/comment/delete/{id}")
    public String deleteEpisodeComment(@PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        this.commentService.delete(comment);
        Long episodeId = comment.getWebtoonEpisode().getId();
        return String.format("redirect:/webtoon/episode/%d", episodeId);
    }
}
