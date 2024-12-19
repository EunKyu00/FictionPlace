package com.example.fiction_place1.domain.favorite.service;

import com.example.fiction_place1.domain.favorite.entity.Favorite;
import com.example.fiction_place1.domain.favorite.repository.FavoriteRepository;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SiteUserRepository siteUserRepository;
    private final WebToonRepository webToonRepository;

    // 관심 작품 등록 또는 해제
    public void toggleFavorite(SiteUser siteUser, Long webtoonId) {
        WebToon webToon = webToonRepository.findById(webtoonId).orElse(null);

        if (webToon == null) {
            throw new IllegalArgumentException("웹툰을 찾을 수 없습니다.");
        }

        // 사용자가 이미 관심 작품에 등록되어 있는지 확인
        List<Favorite> favorites = favoriteRepository.findBySiteUserAndWebtoon(siteUser, webToon);  // 수정된 부분

        if (!favorites.isEmpty()) {
            // 이미 등록되어 있으면, 즐겨찾기 해제
            favoriteRepository.delete(favorites.get(0));  // 수정된 부분
        } else {
            // 없으면, 관심 작품 추가
            Favorite favorite = new Favorite();
            favorite.setSiteUser(siteUser);
            favorite.setWebtoon(webToon);  // 수정된 부분
            favoriteRepository.save(favorite);
        }
    }

    // 사용자가 즐겨찾기한 웹툰 리스트 가져오기
    public List<WebToon> getFavoriteWebToons(SiteUser siteUser) {
        List<Favorite> favorites = favoriteRepository.findBySiteUser(siteUser);
        return favorites.stream()
                .map(Favorite::getWebtoon)  // 수정된 부분
                .collect(Collectors.toList());
    }
}
