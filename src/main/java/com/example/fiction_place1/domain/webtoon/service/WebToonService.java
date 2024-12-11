package com.example.fiction_place1.domain.webtoon.service;

import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.repository.GenreTypeRepository;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebToonService {

    private final WebToonRepository webToonRepository;
    private final GenreTypeRepository genreTypeRepository;
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

    public List<WebToon> getAllWebtoons() {
        return webToonRepository.findAll();  // 모든 웹툰 목록 조회
    }
    public List<WebToon> getWebtoonsByUser(SiteUser siteUser) {
        return webToonRepository.findBySiteUser(siteUser);  // 사용자별 웹툰을 조회하는 쿼리
    }
    // 웹툰 ID로 웹툰을 찾는 메서드
    public WebToon findById(Long id) {
        return webToonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("웹툰을 찾을 수 없습니다. id=" + id));
    }
//    public List<WebToon> findBySiteUser(SiteUser siteUser) {
//        return webToonRepository.findBySiteUser(siteUser);
//    }

//    // 사용자 정보 찾기
//    public SiteUser findUserById(Long userId) {
//        return siteUserRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. id=" + userId));
//    }
//
//    // 웹툰 리스트를 반환하는 메서드
//    public List<WebToon> findAllWebToons() {
//        return webToonRepository.findAll();
//    }
//    // 사용자가 소유한 웹툰 목록을 가져오는 메서드
//    public List<WebToon> findWebToonsByUser(Long userId) {
//        return webToonRepository.findBySiteUserId(userId);
//    }


}


