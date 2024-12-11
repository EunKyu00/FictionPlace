package com.example.fiction_place1.domain.webtoon.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class WebToonForm {
    @NotBlank(message = "작품 제목을 입력하세요.")
    private String title;

    private Long genreTypeId;  // 장르 타입 ID

    @NotBlank(message = "작품 줄거리를 입력하세요.")
    private String content;

    private MultipartFile thumbnailImg;  // 대표 이미지 파일
}
