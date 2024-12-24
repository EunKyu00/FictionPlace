package com.example.fiction_place1.domain.board.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.form.BoardForm;
import com.example.fiction_place1.domain.board.repository.BoardRepository;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.repository.BoardTypeRepository;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardTypeRepository boardTypeRepository;

    // 게시판 타입에 맞는 게시글 조회
    public Page<Board> getBoardType(Long boardTypeId, Pageable pageable) {
        BoardType boardType = boardTypeRepository.findById(boardTypeId)
                .orElseThrow(() -> new EntityNotFoundException("BoardType not found"));
        return boardRepository.findByBoardTypeOrderByCreatedDateDesc(boardType, pageable);
    }

    public void createFreeBoard(String title, String content, Long boardTypeId, User user){
        BoardType boardType = boardTypeRepository.findById(boardTypeId)
                .orElseThrow(() -> new EntityNotFoundException("BoardType not found"));

        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setBoardType(boardType);

        // 게시글 작성자 설정
        if (user instanceof SiteUser) {
            SiteUser siteUser = (SiteUser) user;
            board.setSiteUser(siteUser);
        } else if (user instanceof CompanyUser) {
            CompanyUser companyUser = (CompanyUser) user;
            board.setCompanyUser(companyUser);
        }
        this.boardRepository.save(board);
    }

    public Board getBoard(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
    }

    public void modify(Board board, String title, String content){
        board.setTitle(title);
        board.setContent(content);
        this.boardRepository.save(board);
    }

    public void delete(Board board){
        if (boardRepository.existsById(board.getId())) {
            this.boardRepository.delete(board);
        } else {
            throw new EntityNotFoundException("Board not found for deletion");
        }
    }
    // 게시글 검색 기능
    public List<Board> searchBoards(String keyword) {
        return boardRepository.findByTitleContainingOrContentContainingOrCompanyUser_CompanyNameContainingOrSiteUser_NicknameContaining(
                keyword, keyword, keyword, keyword);
    }

}


