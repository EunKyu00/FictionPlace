package com.example.fiction_place1.domain.favorite.entity;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
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
public class Favorite extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "site_user_id", nullable = true)
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "company_user_id", nullable = true)
    private CompanyUser companyUser;

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private WebToon webtoon;
}
