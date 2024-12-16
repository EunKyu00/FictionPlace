package com.example.fiction_place1.domain.board.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.repository.BoardRepository;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.repository.BoardTypeRepository;
import com.example.fiction_place1.domain.comment.repository.CommentRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final SiteUserRepository siteUserRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CommentRepository commentRepository;

    // 게시판 타입에 맞는 게시글 조회
    public Page<Board> getBoardType(Long boardTypeId, Pageable pageable) {
        BoardType boardType = boardTypeRepository.findById(boardTypeId)
                .orElseThrow(() -> new RuntimeException("BoardType not found"));
        return boardRepository.findByBoardTypeOrderByCreatedDateDesc(boardType, pageable);
    }

    public void createFreeBoard(String title, String content, Long boardTypeId, User user){

        BoardType boardType = boardTypeRepository.findById(boardTypeId)
                .orElseThrow(() -> new RuntimeException("BoardType not found"));

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
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return board;
    }
    public void modify(Board board, String title, String content){
        board.setTitle(title);
        board.setContent(content);
        this.boardRepository.save(board);
    }
    public void delete(Board board){
        this.boardRepository.delete(board);
    }
    // 추천 수 증가 또는 감소
    public void updateLikes(Long boardId, boolean increase) {
        Board board = getBoard(boardId);
        if (increase) {
            board.setLikes(board.getLikes() + 1);
        } else {
            board.setLikes(board.getLikes() - 1);
        }
        boardRepository.save(board);  // 게시글 저장
    }

    //조회수증가로직
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }

    public Board incrementHit(Long boardId) {
        Board board = findById(boardId);
        board.setHit(board.getHit() + 1);
        return boardRepository.save(board);
    }
}


