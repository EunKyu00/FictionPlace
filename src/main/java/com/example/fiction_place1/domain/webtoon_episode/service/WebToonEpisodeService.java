package com.example.fiction_place1.domain.webtoon_episode.service;

import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import com.example.fiction_place1.domain.webtoon.service.FileService;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import com.example.fiction_place1.domain.webtoon_episode.entity.EpisodeImage;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.repository.EpisodeImageRepository;
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
public class WebToonEpisodeService {
    private final WebToonEpisodeRepository webToonEpisodeRepository;
    private final WebToonService webToonService;
    private final FileService fileService; // FileService 사용하여 이미지 처리
    private final EpisodeImageRepository episodeImageRepository;
    private final WebToonRepository webToonRepository;

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

    public List<WebToonEpisode> findSelectedWebtoonEpisodes() {
        // 예시: "isSelected" 필드가 true인 웹툰만 가져오기
        return webToonEpisodeRepository.findByIsSelectedTrue();
    }

    public WebToonEpisode save(WebToonEpisode episode) {
        return webToonEpisodeRepository.save(episode); // JpaRepository의 save 메소드 사용
    }

    public List<WebToonEpisode> findByIds(List<Long> episodeIds) {
        return webToonEpisodeRepository.findAllById(episodeIds); // 해당 아이디 목록으로 회차를 조회
    }
    public void deleteEpisode(WebToonEpisode webToonEpisode){
        this.webToonEpisodeRepository.delete(webToonEpisode);
    }

    public WebToonEpisode getWebtoonEpisode(Long id){
        WebToonEpisode webToonEpisode = webToonEpisodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Episode not found"));
        return webToonEpisode;
    }

    public void modifyWebToonEpisode(Long episodeId, String title, MultipartFile[] episodeImages, MultipartFile thumbnailImg) throws IOException {
        // WebToonEpisode 수정하려는 에피소드 가져오기
        WebToonEpisode webToonEpisode = webToonEpisodeRepository.findById(episodeId).orElse(null);
        if (webToonEpisode == null) {
            return; // 에피소드가 없으면 리턴
        }

        // 제목 수정
        webToonEpisode.setTitle(title);

        // 썸네일 이미지 수정 (Optional)
        if (thumbnailImg != null && !thumbnailImg.isEmpty()) {
            // 새 썸네일 이미지 업로드 처리
            String thumbnailUrl = fileService.uploadImage(thumbnailImg);
            webToonEpisode.setThumbnailImg(thumbnailUrl);
        }


        // 기존 이미지 수정 (Optional)
        if (episodeImages != null && episodeImages.length > 0) {
            // 새로운 에피소드 이미지 추가
            for (MultipartFile file : episodeImages) {
                EpisodeImage episodeImage = new EpisodeImage();
                episodeImage.setEpisode(webToonEpisode); // 해당 WebToonEpisode에 연결

                // 이미지 업로드 후 URL 받기
                String imageUrl = fileService.uploadImage(file);
                episodeImage.setImageUrl(imageUrl);

                // 새로운 에피소드 이미지 리스트에 추가
                webToonEpisode.getEpisodeImages().add(episodeImage);
            }
        }


        // 수정된 WebToonEpisode 저장
        webToonEpisodeRepository.save(webToonEpisode);
    }

}

