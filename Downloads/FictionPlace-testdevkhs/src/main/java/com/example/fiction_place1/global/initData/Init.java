package com.example.fiction_place1.global.initData;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.service.BoardTypeService;
import com.example.fiction_place1.domain.genre_type.service.GenreTypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class Init {
    @Bean
    CommandLineRunner initData(BoardTypeService boardTypeService) {

        return args -> {
            boardTypeService.join("자유게시판");
            boardTypeService.join("채용공고");
        };
    }

    @Bean
    CommandLineRunner initGenreData(GenreTypeService genreTypeService) {
        return args -> {
            genreTypeService.join("액션");
            genreTypeService.join("로맨스");
            genreTypeService.join("판타지");
            genreTypeService.join("스릴러");
            genreTypeService.join("성인");
            genreTypeService.join("드라마");
            genreTypeService.join("개그");
        };
    }
}