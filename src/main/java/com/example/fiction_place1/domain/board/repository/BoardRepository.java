package com.example.fiction_place1.domain.board.repository;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByBoardTypeOrderByCreatedDateDesc(BoardType boardType, Pageable pageable);
    // 제목, 내용, 회사회원, 일반회원으로 검색
    List<Board> findByTitleContainingOrContentContainingOrCompanyUser_CompanyNameContainingOrSiteUser_NicknameContaining(
            String title, String content, String companyName, String nickname);
}
