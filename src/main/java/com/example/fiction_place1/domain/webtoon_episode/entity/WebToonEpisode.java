package com.example.fiction_place1.domain.webtoon_episode.entity;


import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebToonEpisode extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    @NotNull
    private WebToon webToon;

    private String title;

    private String content;

    private String thumbnailImg;
}
