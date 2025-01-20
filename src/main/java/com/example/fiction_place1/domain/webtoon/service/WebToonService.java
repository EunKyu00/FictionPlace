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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void createWebToon(String title, String content, Long genreTypeId, SiteUser siteUser, MultipartFile thumbnailImg) throws IOException {

        // GenreType 객체 조회
        GenreType genreType = this.genreTypeRepository.findById(genreTypeId)
                .orElseThrow(() -> new RuntimeException("GenreType not found"));

        WebToon webToon = new WebToon();

        // 웹툰 정보 설정
        webToon.setTitle(title);
        webToon.setContent(content);
        webToon.setGenreType(genreType);

        // 대표 이미지 경로 설정
        if (thumbnailImg != null && !thumbnailImg.isEmpty()) {
            String thumbnailUrl = fileService.uploadImage(thumbnailImg);
            webToon.setThumbnailImg(thumbnailUrl);
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

    public Page<WebToon> findSelectedWebtoonsWithPagination(Pageable pageable) {
        return webToonRepository.findByIsSelectedTrue(pageable);
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

    public Page<WebToon> searchWebToon(String keyword, Pageable pageable) {
        return webToonRepository.findByTitleContainingOrSiteUser_NicknameContaining(keyword,keyword,pageable);
    }

    public Page<WebToon> findAll(Pageable pageable) {
        return webToonRepository.findAll(pageable);
    }

    // 장르 ID가 있으면 해당 장르의 웹툰만 조회
    public Page<WebToon> getWebtoonsByGenreId(Long genreId,Pageable pageable) {
        return webToonRepository.findByGenreTypeId(genreId,pageable);
    }

    // 장르별 웹툰 추천순 페이징 처리
    public Page<WebToon> getWebtoonsByGenreSortedByLikes(Long genreTypeId, Pageable pageable) {
        return webToonRepository.findByGenreType_IdOrderByLikesDesc(genreTypeId, pageable);
    }

    // 전체 목록 추천순 페이징 처리
    public Page<WebToon> getWebtoonsSortedByLikes(Pageable pageable) {
        // Pageable에 Sort를 포함시켜서 호출
        Pageable sortedByLikes = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("likes")));
        return webToonRepository.findAll(sortedByLikes);
    }


}



