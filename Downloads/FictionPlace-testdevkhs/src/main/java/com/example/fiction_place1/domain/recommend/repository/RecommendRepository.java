package com.example.fiction_place1.domain.recommend.repository;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.recommend.entity.Recommend;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {
    boolean existsBySiteUserAndBoard(SiteUser siteUser, Board board);
    boolean existsByCompanyUserAndBoard(CompanyUser companyUser, Board board);

    boolean existsBySiteUserAndWebToon(SiteUser siteUser, WebToon webToon);
    boolean existsByCompanyUserAndWebToon(CompanyUser companyUser, WebToon webToon);
}
