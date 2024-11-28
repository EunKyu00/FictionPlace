package com.example.fiction_place1.domain.board.entity;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "company_user_id", nullable = true)
    private CompanyUser companyUser;

    @ManyToOne
    @JoinColumn(name = "site_user_id", nullable = true)
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "board_type_id")
    private BoardType boardType;

    private String title;
    private String content;
    private Integer likes = 0;
    private Integer hit = 0;
}

