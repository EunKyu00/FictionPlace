package com.example.fiction_place1.domain.genre_type.service;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.repository.GenreTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreTypeService {
    private final GenreTypeRepository genreTypeRepository;

    public void join(String name) {
        if (!genreTypeRepository.existsByGenreTypename(name)) {
            GenreType genreType = GenreType.builder()
                    .genreTypename(name)
                    .build();
            this.genreTypeRepository.save(genreType);
        }
    }

    public List<GenreType> getAllGenres() {
        return genreTypeRepository.findAll();  // 모든 장르 타입을 가져오는 메서드
    }
}

