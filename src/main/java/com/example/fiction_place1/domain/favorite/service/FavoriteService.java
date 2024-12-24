package com.example.fiction_place1.domain.favorite.service;

import com.example.fiction_place1.domain.favorite.entity.Favorite;
import com.example.fiction_place1.domain.favorite.repository.FavoriteRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SiteUserRepository siteUserRepository;
    private final WebToonRepository webToonRepository;

    // 관심 작품 등록 또는 해제
    public void toggleFavorite(Long webtoonId, User user) {
        WebToon webToon = webToonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException("웹툰을 찾을 수 없습니다."));

        Favorite existingFavorite = null;

        if (user instanceof SiteUser) {
            // SiteUser의 즐겨찾기 확인
            existingFavorite = favoriteRepository.findBySiteUserAndWebtoon((SiteUser) user, webToon).stream().findFirst().orElse(null);
        } else if (user instanceof CompanyUser) {
            // CompanyUser의 즐겨찾기 확인
            existingFavorite = favoriteRepository.findByCompanyUserAndWebtoon((CompanyUser) user, webToon).stream().findFirst().orElse(null);
        }

        if (existingFavorite != null) {
            this.favoriteRepository.delete(existingFavorite);
        } else {
            Favorite favorite = new Favorite();
            favorite.setWebtoon(webToon);

            if (user instanceof SiteUser) {
                SiteUser siteUser = (SiteUser) user;
                favorite.setSiteUser(siteUser);
            } else if (user instanceof CompanyUser) {
                CompanyUser companyUser = (CompanyUser) user;
                favorite.setCompanyUser(companyUser);
            }
            this.favoriteRepository.save(favorite);
        }
    }

    // 사용자가 즐겨찾기한 웹툰 리스트 가져오기
    public List<WebToon> getFavoriteWebToons(User user) {
        List<Favorite> favorites;
        // SiteUser일 경우
        if (user instanceof SiteUser) {
            SiteUser siteUser = (SiteUser) user;
            favorites = favoriteRepository.findBySiteUser(siteUser);
        }
        // CompanyUser일 경우
        else if (user instanceof CompanyUser) {
            CompanyUser companyUser = (CompanyUser) user;
            favorites = favoriteRepository.findByCompanyUser(companyUser);
        } else {
            // 사용자가 SiteUser나 CompanyUser가 아닌 경우
            throw new IllegalArgumentException("사용자가 유효하지 않습니다.");
        }

        return favorites.stream()
                .map(Favorite::getWebtoon)
                .collect(Collectors.toList());
    }

}
