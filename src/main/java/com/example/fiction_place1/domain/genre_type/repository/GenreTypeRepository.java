package com.example.fiction_place1.domain.genre_type.repository;

import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreTypeRepository extends JpaRepository<GenreType,Long> {
    // 해당 이름을 가진 GenreType이 존재하는지 확인하는 메서드
    boolean existsByGenreTypename(String genreTypename);

}
