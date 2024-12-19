package com.example.fiction_place1.domain.favorite.repository;

import com.example.fiction_place1.domain.favorite.entity.Favorite;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    // 특정 사용자가 특정 웹툰을 즐겨찾기 했는지 확인하는 메서드
    List<Favorite> findBySiteUserAndWebtoon(SiteUser siteUser, WebToon webtoon);

    // 특정 사용자가 즐겨찾기한 모든 웹툰 목록 가져오기
    List<Favorite> findBySiteUser(SiteUser siteUser);

    // 특정 사용자가 즐겨찾기한 특정 웹툰 삭제
    void deleteBySiteUserAndWebtoon(SiteUser siteUser, WebToon webtoon);  // 수정된 부분
}
