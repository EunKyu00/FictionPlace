package com.example.fiction_place1.domain.webtoon_episode.controller;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import com.example.fiction_place1.domain.webtoon_episode.entity.EpisodeImage;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.form.WebToonEpisodeForm;
import com.example.fiction_place1.domain.webtoon_episode.service.WebToonEpisodeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebToonEpisodeController {
    private final WebToonEpisodeService webToonEpisodeService;
    private final WebToonService webToonService;

    // 회차 등록 페이지
    @GetMapping("/webtoon/episodes/create/{id}")
    public String getWebToonEpisodeRegistrationPage(@PathVariable("id") Long webtoonId, Model model, HttpSession session) {
        // 세션에서 현재 로그인한 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser != null) {
            List<WebToon> webtoons = webToonService.getWebtoonsByUser(siteUser);
            model.addAttribute("webtoons", webtoons);
        }
        // 선택된 웹툰 정보 가져오기
        WebToon webToon = webToonService.findById(webtoonId);
        model.addAttribute("webtoon", webToon);

        // 웹툰 ID를 모델에 추가
        model.addAttribute("webtoonId", webtoonId);

        return "webtoon_episode_create";
    }

    // 회차 등록 처리
    @PostMapping("/webtoon/episode/create/{id}")
    public String createWebToonEpisode(
            @PathVariable("id") Long webtoonId,
            @RequestParam("title") String title,
            @RequestParam(value = "episodeImages", required = false) MultipartFile[] episodeImages,
            @RequestParam(value = "thumbnailImg", required = false) MultipartFile thumbnailImg,
            HttpSession session) throws IOException {

        // 세션에서 로그인한 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null) {
            return "redirect:/"; // 로그인 페이지로 리다이렉트
        }
        // 회차 등록 처리
        webToonEpisodeService.createWebToonEpisode(webtoonId, title, episodeImages, thumbnailImg);

        // 등록 후 해당 웹툰 회차 목록 페이지로 리다이렉트
        return "redirect:/webtoon/episode/list/" + webtoonId;
    }

    // 웹툰 회차 목록 페이지
    @GetMapping("/webtoon/episode/list/{id}")
    public String getWebToonEpisodeList(@PathVariable("id") Long webtoonId, Model model) {
        // 웹툰 정보 가져오기
        WebToon webtoon = webToonService.findById(webtoonId);

        // 모델에 웹툰 정보와 회차 목록 추가
        model.addAttribute("webtoon", webtoon);
        model.addAttribute("episodes", webToonEpisodeService.getEpisodesByWebtoonId(webtoonId));
        model.addAttribute("webtoonId", webtoonId);

        return "episode_list"; // 회차 목록 페이지 HTML 템플릿
    }
    // 에피소드 상세 페이지
    @GetMapping("/webtoon/episode/{id}")
    public String getWebToonEpisodeDetail(@PathVariable("id") Long episodeId, Model model) {
        // 에피소드 정보 가져오기
        WebToonEpisode webToonEpisode = webToonEpisodeService.findById(episodeId);

        // 에피소드 이미지 가져오기
        List<EpisodeImage> episodeImages = webToonEpisodeService.getImagesByEpisodeId(episodeId);

        // 모델에 데이터 추가
        model.addAttribute("episode", webToonEpisode);
        model.addAttribute("images", episodeImages);

        return "episode_detail"; // 에피소드 상세 페이지 HTML 템플릿
    }
}
