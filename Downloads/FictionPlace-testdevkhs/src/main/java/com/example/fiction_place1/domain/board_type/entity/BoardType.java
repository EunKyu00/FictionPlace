package com.example.fiction_place1.domain.board_type.entity;


import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BoardType extends BaseEntity {

    private String boardTypeName;

    @OneToMany(mappedBy = "boardType")
    private List<Board> boards;
}
