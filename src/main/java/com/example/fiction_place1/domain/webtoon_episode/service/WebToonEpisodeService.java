package com.example.fiction_place1.domain.webtoon_episode.service;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.service.FileService;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import com.example.fiction_place1.domain.webtoon_episode.entity.EpisodeImage;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.repository.EpisodeImageRepository;
import com.example.fiction_place1.domain.webtoon_episode.repository.WebToonEpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebToonEpisodeService {
    private final WebToonEpisodeRepository webToonEpisodeRepository;
    private final WebToonService webToonService;
    private final FileService fileService; // FileService 사용하여 이미지 처리
    private final EpisodeImageRepository episodeImageRepository;

    public WebToonEpisode createWebToonEpisode(Long webtoonId, String title, MultipartFile[] episodeImages, MultipartFile thumbnailImg) throws IOException {
        // 웹툰 정보 조회
        WebToon webToon = webToonService.findById(webtoonId);
        if (webToon == null) {
            throw new IllegalArgumentException("웹툰이 존재하지 않습니다.");
        }

        // 회차 생성
        WebToonEpisode webToonEpisode = new WebToonEpisode();
        webToonEpisode.setTitle(title);
        webToonEpisode.setWebToon(webToon);

        // 회차 저장
        webToonEpisode = webToonEpisodeRepository.save(webToonEpisode);

        // 썸네일 이미지 처리
        if (thumbnailImg != null) {
            String thumbnailPath = fileService.uploadImage(thumbnailImg);  // 썸네일 이미지 업로드 후 URL 반환
            webToonEpisode.setThumbnailImg(thumbnailPath);
            webToonEpisodeRepository.save(webToonEpisode); // 썸네일 업데이트
        }

        // 이미지 순서 추가
        if (episodeImages != null && episodeImages.length > 0) {
            int order = 1; // 이미지 순서 시작
            for (MultipartFile image : episodeImages) {
                String imagePath = fileService.uploadImage(image);  // 이미지 업로드 후 URL 반환

                // 나머지 이미지를 EpisodeImage 테이블에 저장
                EpisodeImage episodeImage = new EpisodeImage();
                episodeImage.setImageUrl(imagePath);
                episodeImage.setEpisode(webToonEpisode);
                episodeImage.setOrder(order++); // 순서 지정
                episodeImageRepository.save(episodeImage);
            }
        }

        return webToonEpisode;
    }

    // 기존 코드 그대로 유지
    public List<WebToonEpisode> getEpisodesByWebtoonId(Long webtoonId) {
        return webToonEpisodeRepository.findByWebToonId(webtoonId);
    }

    public WebToonEpisode findById(Long episodeId) {
        return webToonEpisodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 에피소드가 존재하지 않습니다."));
    }

    public List<EpisodeImage> getImagesByEpisodeId(Long episodeId) {
        return episodeImageRepository.findByEpisodeIdOrderByOrderAsc(episodeId); // order 필드 기준으로 오름차순 정렬
    }

}

