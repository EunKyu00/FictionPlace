package com.example.fiction_place1.domain.comment.entity;


import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "site_user_id", nullable = true)
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "company_user_id", nullable = true)
    private CompanyUser companyUser;

    @ManyToOne
    @JoinColumn(name = "webtoon_episode_id",  nullable = true)
    private WebToonEpisode webtoonEpisode;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = true )
    private Board board;

    private String content;

    private Integer likes = 0;

}

