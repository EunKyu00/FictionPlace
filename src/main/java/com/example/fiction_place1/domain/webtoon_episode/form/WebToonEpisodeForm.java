package com.example.fiction_place1.domain.webtoon_episode.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class WebToonEpisodeForm {

    @NotNull(message = "웹툰을 선택하세요.")
    private Long webtoonId;  // 웹툰 ID

    @NotBlank(message = "회차 제목을 입력하세요.")
    private String title;  // 회차 제목

    private List<MultipartFile> episodeImages;  // 여러 이미지 업로드 (선택 사항)

    @NotNull(message = "회차 순서를 입력하세요.")
    @Min(value = 1, message = "회차 순서는 1 이상의 값이어야 합니다.")
    private Integer episodeOrder;  // 회차 순서
}
