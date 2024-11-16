package com.example.fiction_place1.domain.webtoon.entity;





import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WebToon extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "site_user_id")
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "genre_type_id")
    private GenreType genreType;

    @OneToMany(mappedBy = "webToon")
    private List<WebToonEpisode> webtoonEpisodes;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String thumbnailImg;

    private Integer likes = 0;
    private Integer hit = 0;

}

