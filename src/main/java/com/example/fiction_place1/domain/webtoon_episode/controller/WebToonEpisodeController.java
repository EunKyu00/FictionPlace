package com.example.fiction_place1.domain.webtoon_episode.controller;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import com.example.fiction_place1.domain.webtoon_episode.entity.EpisodeImage;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.service.WebToonEpisodeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class WebToonEpisodeController {
    private final WebToonEpisodeService webToonEpisodeService;
    private final WebToonService webToonService;

    @GetMapping("/")
    public String getAllWebtoons(Model model) {
        // 데이터베이스에서 선택된 웹툰 정보 가져오기
        List<WebToon> selectedWebtoons = webToonService.findSelectedWebtoons();

        if (selectedWebtoons.isEmpty()) {
            model.addAttribute("message", "등록된 웹툰이 없습니다.");
        } else {
            model.addAttribute("selectedWebtoons", selectedWebtoons);
        }
        return "webtoon_list"; // 웹툰 리스트 템플릿 반환
    }

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

        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null) {
            return "redirect:/login/user";
        }
        webToonEpisodeService.createWebToonEpisode(webtoonId, title, episodeImages, thumbnailImg);

        return "redirect:/webtoon/episode/list/" + webtoonId;
    }

    // 작가 사용자 웹툰 회차 목록 페이지
    @GetMapping("/webtoon/episode/list/{id}")
    public String getWebToonEpisodeList(@PathVariable("id") Long webtoonId, Model model) {
        // 웹툰 정보 가져오기
        WebToon webtoon = webToonService.findById(webtoonId);

        // 모델에 웹툰 정보와 회차 목록 추가
        model.addAttribute("webtoon", webtoon);
        model.addAttribute("episodes", webToonEpisodeService.getEpisodesByWebtoonId(webtoonId));
        model.addAttribute("webtoonId", webtoonId);

        return "episode_list";
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

        return "episode_detail";
    }

    //회차 메인페이지 등록 처리
    @PostMapping("/webtoon/episodes/mainpage")
    public String registerToMainPage(@RequestParam("webtoonId") Long webtoonId,
                                     @RequestParam("selectedEpisodes") List<Long> selectedEpisodes,
                                     Model model) {

        WebToon webToon = webToonService.findById(webtoonId);
        List<WebToonEpisode> selectedWebtoonEpisodes = webToonEpisodeService.findByIds(selectedEpisodes);

        if (webToon != null) {
            // 웹툰을 선택된 상태로 업데이트
            webToon.setSelected(true);
            webToonService.save(webToon); // 업데이트된 웹툰 저장
        }
        // 선택된 회차들을 메인 페이지에 등록
        for (WebToonEpisode episode : selectedWebtoonEpisodes) {
            episode.setSelected(true); // 선택된 상태로 업데이트
            webToonEpisodeService.save(episode); // 회차 업데이트
        }

        return "redirect:/"; // 메인 페이지로 리다이렉트
    }

    // 메인 페이지 웹툰 리스트
    @GetMapping("/main/page/webtoon/episode/{id}")
    public String mainPageWebToonEpisodeList(@PathVariable("id") Long webtoonId, Model model) {

        List<WebToon> selectedWebtoons = webToonService.findSelectedWebtoons();
        Map<Long, List<WebToonEpisode>> episodesByWebtoon = new HashMap<>();

        // 선택된 에피소드들만 가져오기
        List<WebToonEpisode> selectedWebtoonEpisodes = webToonEpisodeService.findSelectedWebtoonEpisodes();

        // 각 웹툰에 대해 해당하는 선택된 에피소드 목록 가져오기
        WebToon targetWebtoon = null; // 선택된 웹툰을 저장할 변수
        for (WebToon webToon : selectedWebtoons) {
            if (webToon.getId().equals(webtoonId)) {
                targetWebtoon = webToon; // 요청받은 웹툰 ID와 일치하는 웹툰 저장
            }

            // 해당 웹툰에 맞는 선택된 에피소드들 필터링
            List<WebToonEpisode> episodesForWebtoon = selectedWebtoonEpisodes.stream()
                    .filter(episode -> episode.getWebToon().getId().equals(webToon.getId()))
                    .collect(Collectors.toList());

            episodesByWebtoon.put(webToon.getId(), episodesForWebtoon); // 웹툰 ID를 키로, 에피소드 목록을 값으로 저장
        }

        if (targetWebtoon == null) {
            throw new IllegalArgumentException("Invalid WebToon ID");
        }
        // 웹툰 정보 가져오기
        WebToon webtoon = webToonService.findById(webtoonId);

        model.addAttribute("webtoon", webtoon);
        model.addAttribute("webtoon", targetWebtoon); // 선택된 웹툰을 모델에 추가
        model.addAttribute("episodesByWebtoon", episodesByWebtoon); // 에피소드 목록 추가

        return "main_page_episode_detail";
    }
}
