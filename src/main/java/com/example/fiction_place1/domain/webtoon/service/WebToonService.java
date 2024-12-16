package com.example.fiction_place1.domain.webtoon.service;

import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.repository.GenreTypeRepository;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.repository.WebToonEpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebToonService {

    private final WebToonRepository webToonRepository;
    private final GenreTypeRepository genreTypeRepository;
    private final WebToonEpisodeRepository webToonEpisodeRepository;
    private final SiteUserRepository siteUserRepository;
    private final FileService fileService; // 파일 업로드를 위한 서비스

    public void createWebToon(String title, String content, Long genreTypeId, SiteUser siteUser, String thumbnailPath) throws IOException {

        // GenreType 객체 조회
        GenreType genreType = this.genreTypeRepository.findById(genreTypeId)
                .orElseThrow(() -> new RuntimeException("GenreType not found"));

        WebToon webToon = new WebToon();

        // 웹툰 정보 설정
        webToon.setTitle(title);
        webToon.setContent(content);
        webToon.setGenreType(genreType);

        // 대표 이미지 경로 설정
        if (thumbnailPath != null && !thumbnailPath.isEmpty()) {
            webToon.setThumbnailImg(thumbnailPath);  // 경로 설정
        }

        // 작성자 설정
        webToon.setSiteUser(siteUser);

        // 웹툰 저장
        webToonRepository.save(webToon);
    }

    public List<WebToon> getWebtoonsByUser(SiteUser siteUser) {
        return webToonRepository.findBySiteUser(siteUser);  // 사용자별 웹툰을 조회하는 쿼리
    }
    // 웹툰 ID로 웹툰을 찾는 메서드
    public WebToon findById(Long id) {
        return webToonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("웹툰을 찾을 수 없습니다. id=" + id));
    }

    public List<WebToon> findSelectedWebtoons() {
        // 예시: "isSelected" 필드가 true인 웹툰만 가져오기
        return webToonRepository.findByIsSelectedTrue();
    }
    public WebToon save(WebToon webToon) {
        return webToonRepository.save(webToon);
    }

    public List<WebToon> getWebtoonsWithSelectedEpisodes(SiteUser siteUser) {
        // siteUser와 관련된 모든 웹툰을 가져옵니다.
        List<WebToon> allWebToons = webToonRepository.findBySiteUser(siteUser);

        // 각 웹툰의 에피소드 중에서 isSelected가 true인 것이 하나라도 있으면 해당 웹툰만 필터링
        return allWebToons.stream()
                .filter(webToon -> webToon.getWebtoonEpisodes().stream()
                        .anyMatch(episode -> episode.isSelected())) // 에피소드 중 isSelected가 true인 것이 하나라도 있으면
                .collect(Collectors.toList());
    }


}


