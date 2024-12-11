package com.example.fiction_place1.domain.webtoon.repository;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebToonRepository extends JpaRepository<WebToon,Long> {
    List<WebToon> findBySiteUser(SiteUser siteUser);  // 사용자별 웹툰을 조회하는 메서드
    List<WebToon> findBySiteUserId(Long userId);
}
