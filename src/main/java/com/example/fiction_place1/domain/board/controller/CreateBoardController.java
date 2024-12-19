package com.example.fiction_place1.domain.board.controller;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.form.BoardForm;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.service.BoardTypeService;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.service.CommentService;
import com.example.fiction_place1.domain.recommend.service.RecommendService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CreateBoardController {
    private final BoardService boardService;
    private final BoardTypeService boardTypeService;
    private final CommentService commentService;
    private final RecommendService recommendService;

    // 게시판 목록 페이지
    @GetMapping("/board")
    public String boardList(
            @RequestParam(value = "boardTypeId", defaultValue = "1") Long boardTypeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        // 모든 게시판 타입 목록 가져오기
        List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
        model.addAttribute("boardTypes", boardTypes);
        // 선택한 게시판 타입 가져오기
        BoardType selectedBoardType = boardTypeService.findById(boardTypeId);
        model.addAttribute("selectedBoardType", selectedBoardType);
        // 페이징 처리된 게시글 목록 가져오기
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardPage = boardService.getBoardType(boardTypeId, pageable);
        // 게시글 목록 및 페이징 정보 모델에 추가
        model.addAttribute("boardList", boardPage.getContent()); // 게시글 리스트
        model.addAttribute("currentPage", boardPage.getNumber()); // 현재 페이지
        model.addAttribute("totalElements", boardPage.getTotalElements());
        model.addAttribute("size", boardPage.getSize()); // 페이지 크기
        model.addAttribute("totalPages", boardPage.getTotalPages()); // 전체 페이지 수

        // 선택한 게시판 타입 모델에 추가
        model.addAttribute("boardTypeId", boardTypeId);
        return "board_list";
    }

    // 게시글 작성 페이지
    @GetMapping("/board/create")
    public String freeBoardCreate(BoardForm boardForm, Model model) {
        List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
        model.addAttribute("boardTypes", boardTypes);
        return "create_board";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/create")
    public String createFreeBoard(@Valid BoardForm boardForm, BindingResult bindingResult,
                                  HttpSession session,
                                  @RequestParam("boardTypeId") Long boardTypeId,
                                  Model model) {
        model.addAttribute("boardTypeId", boardTypeId);

        // 로그인된 사용자 세션 받아오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 로그인 체크
        if (siteUser == null && companyUser == null) {
            model.addAttribute("errorMessage", "로그인 후 이용해주세요.");
            List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
            model.addAttribute("boardTypes", boardTypes);
            return "create_board"; // 게시글 작성 페이지로 다시 이동
        }

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            List<BoardType> boardTypes = boardTypeService.getAllBoardTypes();
            model.addAttribute("boardTypes", boardTypes);
            model.addAttribute("boardForm", boardForm);
            return "create_board";
        }

        // 일반회원 또는 기업회원 게시글 작성
        if (siteUser != null) {
            model.addAttribute("nickname", siteUser.getNickname());
            boardService.createFreeBoard(boardForm.getTitle(), boardForm.getContent(), boardTypeId, siteUser);
        } else if (companyUser != null) {
            model.addAttribute("companyName", companyUser.getCompanyName());
            boardService.createFreeBoard(boardForm.getTitle(), boardForm.getContent(), boardTypeId, companyUser);
        }

        return "redirect:/board?boardTypeId=" + boardTypeId;
    }


    //게시글 상세
    @GetMapping("/board/detail/{id}")
    public String boardDetail(Model model, @PathVariable("id") Long id, HttpSession session) {
        // 게시글 조회
        Board board = boardService.getBoard(id);
        Board boardHit = boardService.incrementHit(id);
        // 로그인된 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 게시글 작성자인지 확인
        boolean isAuthor = false;
        if (siteUser != null) {
            isAuthor = board.getSiteUser() != null && siteUser.getId().equals(board.getSiteUser().getId());
        } else if (companyUser != null) {
            isAuthor = board.getCompanyUser() != null && companyUser.getId().equals(board.getCompanyUser().getId());
        }

        int commentCount = commentService.getCommentCountForBoard(id); //댓글 갯수 조회

        // 댓글 목록 가져오기
        List<Comment> comments = commentService.getCommentsByBoardId(id); // 정렬된 댓글 가져오기

        // 로그인된 사용자와 비교해 댓글 작성자 여부만 서버에서 필터링
        List<Comment> userComments = comments.stream()
                .filter(comment -> {
                    if (siteUser != null) {
                        return comment.getSiteUser() != null && comment.getSiteUser().getId().equals(siteUser.getId());
                    } else if (companyUser != null) {
                        return comment.getCompanyUser() != null && comment.getCompanyUser().getId().equals(companyUser.getId());
                    }
                    return false;
                }).toList();

        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        model.addAttribute("comment_count", commentCount);
        model.addAttribute("userComments", userComments); // 로그인된 사용자 관련 댓글만 전달
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("loginUser", siteUser);
        model.addAttribute("loginCompanyUser", companyUser);
        model.addAttribute("boardHit", boardHit);

        return "board_detail";
    }

    //게시글 추천
    @PostMapping("/board/recommend/{id}")
    public String recommendBoard(
            @PathVariable("id") Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Board board = boardService.getBoard(id);
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if(siteUser == null && companyUser == null){
            redirectAttributes.addFlashAttribute("message","로그인 후 이용해주세요.");
            return "redirect:/board/detail/" + id;
        }

        // 추천 여부 확인
        boolean hasRecommended = false;

        if (siteUser != null) {
            hasRecommended = recommendService.hasSiteUserRecommended(siteUser, board);
        } else if (companyUser != null) {
            hasRecommended = recommendService.hasCompanyUserRecommended(companyUser, board);
        }

        if (hasRecommended) {
            redirectAttributes.addFlashAttribute("message", "이미 추천하셨습니다!");
        } else {
            if (siteUser != null) {
                recommendService.addSiteUserRecommendation(siteUser, board);
            } else if (companyUser != null) {
                recommendService.addCompanyUserRecommendation(companyUser, board);
            }
            redirectAttributes.addFlashAttribute("message", "추천이 완료되었습니다!");
        }
        return "redirect:/board/detail/" + id;
    }

}


