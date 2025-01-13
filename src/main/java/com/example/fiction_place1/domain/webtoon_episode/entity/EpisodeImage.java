package com.example.fiction_place1.domain.webtoon_episode.entity;

import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EpisodeImage extends BaseEntity {

    @Column(length = 1000) // URL 길이에 맞는 제약 조건 추가 (길이에 따라 조정)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private WebToonEpisode episode;

    @Column(name = "image_order") // imageOrder로 컬럼 이름 변경 (필요시)
    private Integer order; // 이미지 순서를 위한 필드
}

