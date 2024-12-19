package com.example.fiction_place1.domain.recommend.entity;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
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
public class Recommend extends BaseEntity{
    @ManyToOne
    private SiteUser siteUser;

    @ManyToOne
    private CompanyUser companyUser;

    @ManyToOne
    private Board board;

    @ManyToOne
    private WebToon webToon;
}
