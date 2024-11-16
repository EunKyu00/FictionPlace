package com.example.fiction_place1.domain.genre_type.entity;


import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class GenreType extends BaseEntity {
    private String genreTypename;

    @OneToMany(mappedBy = "genreType")
    private List<WebToon> webToons;
}
