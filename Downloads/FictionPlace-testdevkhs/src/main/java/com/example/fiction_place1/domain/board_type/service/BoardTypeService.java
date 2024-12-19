package com.example.fiction_place1.domain.board_type.service;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.repository.BoardTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardTypeService {
    private final BoardTypeRepository boardTypeRepository;

    public void join (String name) {

        if (!boardTypeRepository.existsByBoardTypeName(name)) {
            BoardType boardType = BoardType.builder()
                    .boardTypeName(name)
                    .build();
            this.boardTypeRepository.save(boardType);
        }
    }
    // 모든 게시판 타입을 반환하는 메서드
    public List<BoardType> getAllBoardTypes() {
        return boardTypeRepository.findAll();
    }
    // ID로 게시판 타입을 찾는 메서드
    public BoardType findById(Long id) {
        return boardTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판 타입입니다."));  // 예외 처리
    }
}
