package com.example.fiction_place1.domain.board.repository;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findByBoardType(BoardType boardType);
    Page<Board> findByBoardTypeOrderByCreatedDateDesc(BoardType boardType, Pageable pageable);


}
