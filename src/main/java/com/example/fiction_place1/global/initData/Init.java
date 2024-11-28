package com.example.fiction_place1.global.initData;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.board_type.service.BoardTypeService;
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
}