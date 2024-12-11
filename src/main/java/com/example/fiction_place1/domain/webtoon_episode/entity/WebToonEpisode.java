package com.example.fiction_place1.domain.webtoon_episode.entity;


import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebToonEpisode extends BaseEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "webtoon_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 부모 삭제 시 자식도 삭제
    private WebToon webToon;

    private String title;

    private String thumbnailImg;  // 썸네일 이미지 경로

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true) //에피소드 삭제시 이미지 전부 삭제
    private List<EpisodeImage> episodeImages = new ArrayList<>();
}
