package com.example.fiction_place1.domain.webtoon.repository;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebToonRepository extends JpaRepository<WebToon,Long> {
    List<WebToon> findBySiteUser(SiteUser siteUser);  // 사용자별 웹툰을 조회하는 메서드
    // 모든 선택된 웹툰 가져오기 (페이징 없이)
    List<WebToon> findByIsSelectedTrue();

    // 선택된 웹툰을 페이징하여 가져오기
    Page<WebToon> findByIsSelectedTrue(Pageable pageable);

    List<WebToon> findAllById(Iterable<Long> ids);

    List<WebToon> findByTitleContainingOrSiteUser_NicknameContaining(String title, String nickname);

    Page<WebToon> findAll(Pageable pageable);

    List<WebToon> findByGenreType_IdOrderByLikesDesc(Long genreTypeId);

    // 장르 ID에 따른 웹툰 리스트
    Page<WebToon> findByGenreTypeId(Long genreId,Pageable pageable);
}
