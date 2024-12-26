package com.example.fiction_place1.domain.webtoon.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.repository.GenreTypeRepository;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.repository.WebToonEpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
        WebToon webToon = webToonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WebToon not found"));
        return webToon;
    }

    public List<WebToon> findSelectedWebtoons() {
        // 예시: "isSelected" 필드가 true인 웹툰만 가져오기
        return webToonRepository.findByIsSelectedTrue();
    }

    public WebToon save(WebToon webToon) {
        return webToonRepository.save(webToon);
    }

    public void deleteWebtoon(WebToon webToon) {
        this.webToonRepository.delete(webToon);
    }

    public void modifyWebToon(Long webtoonId, String title, String content, Long genreTypeId, MultipartFile thumbnailImg) throws IOException {
        WebToon webToon = webToonRepository.findById(webtoonId).orElse(null);
        if (webToon == null) {
            return;
        }

        GenreType genreType = this.genreTypeRepository.findById(genreTypeId)
                .orElseThrow(() -> new RuntimeException("GenreType not found"));
        webToon.setTitle(title);
        webToon.setContent(content);
        webToon.setGenreType(genreType);

        if (thumbnailImg != null && !thumbnailImg.isEmpty()) {
            String thumbnailUrl = fileService.uploadImage(thumbnailImg);
            webToon.setThumbnailImg(thumbnailUrl);
        }
        webToonRepository.save(webToon);
    }
    public List<WebToon> searchWebToon(String keyword) {
        return webToonRepository.findByTitleContainingOrSiteUser_NicknameContaining(
                keyword, keyword);
    }
    //전체목록 추천순
    public List<WebToon> getWebtoonsSortedByLikes() {
        return webToonRepository.findAll(Sort.by(Sort.Order.desc("likes"))); // likes 기준 내림차순
    }

    public List<WebToon> getWebtoonsByGenreId(Long genreId) {
        return webToonRepository.findByGenreTypeId(genreId);
    }

    public List<WebToon> findAll(){
        return webToonRepository.findAll();
    }
}


