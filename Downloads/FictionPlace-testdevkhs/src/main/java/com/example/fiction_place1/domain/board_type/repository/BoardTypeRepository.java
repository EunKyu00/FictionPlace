package com.example.fiction_place1.domain.board_type.repository;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTypeRepository extends JpaRepository<BoardType, Long> {
    //    Optional<BoardType> findById(Long id);
// 해당 이름을 가진 BoardType이 존재하는지 확인하는 메서드
    boolean existsByBoardTypeName(String boardTypeName);

}
