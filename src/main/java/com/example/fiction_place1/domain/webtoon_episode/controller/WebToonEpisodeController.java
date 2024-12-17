package com.example.fiction_place1.domain.webtoon_episode.controller;

import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.comment.service.CommentService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.service.FileService;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import com.example.fiction_place1.domain.webtoon_episode.entity.EpisodeImage;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.service.WebToonEpisodeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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
    private final CommentService commentService;
    private final FileService fileService;

    @GetMapping("/")
    public String getAllWebtoons(Model model) {
        // 데이터베이스에서 선택된 웹툰 정보 가져오기
        List<WebToon> selectedWebtoons = webToonService.findSelectedWebtoons();

        // 에피소드 중 isSelected가 true인 에피소드가 하나라도 있는 웹툰만 필터링
        List<WebToon> webtoonsWithSelectedEpisodes = selectedWebtoons.stream()
                .filter(webtoon -> webtoon.getWebtoonEpisodes().stream()
                        .anyMatch(episode -> episode.isSelected())) // isSelected가 true인 에피소드가 있는 웹툰만 남기기
                .collect(Collectors.toList());

        if (webtoonsWithSelectedEpisodes.isEmpty()) {
            model.addAttribute("message", "등록된 웹툰이 없습니다.");
        } else {
            model.addAttribute("selectedWebtoons", webtoonsWithSelectedEpisodes);
        }

        return "webtoon_list";
    }


    // 회차 등록 페이지
    @GetMapping("/webtoon/episodes/create/{id}")
    public String getWebToonEpisodeRegistrationPage(@PathVariable("id") Long webtoonId, Model model, HttpSession session) {
        // 선택된 웹툰 정보 가져오기
        WebToon webToon = webToonService.findById(webtoonId);

        // 세션에서 현재 로그인한 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null) {
            return  "redirect:/login/user";
        }
        // 로그인한 사용자와 웹툰 작성자가 동일한지 확인
        if (!webToon.getSiteUser().getId().equals(siteUser.getId())) {
            return "access_denied";
        }

        model.addAttribute("webtoon", webToon);  // 웹툰 정보 추가
        model.addAttribute("webtoonId", webtoonId);  // 웹툰 ID 추가
        List<WebToon> webtoons = webToonService.getWebtoonsByUser(siteUser);
        model.addAttribute("webtoons", webtoons);  // 해당 사용자 웹툰 목록

        // 로그인한 사용자가 웹툰 작성자가 아니면 접근 거부
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
    public String getWebToonEpisodeList(@PathVariable("id") Long webtoonId, Model model,HttpSession session) {
        // 웹툰 정보 가져오기
        WebToon webtoon = webToonService.findById(webtoonId);
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser == null) {
            return "redirect:/login/user";
        }
        if (!webtoon.getSiteUser().getId().equals(siteUser.getId())){
            return "access_denied";
        }

        // 모델에 웹툰 정보와 회차 목록 추가
        model.addAttribute("webtoon", webtoon);
        model.addAttribute("episodes", webToonEpisodeService.getEpisodesByWebtoonId(webtoonId));
        model.addAttribute("webtoonId", webtoonId);

        return "episode_list";
    }

    // 에피소드 상세 페이지
    @GetMapping("/webtoon/episode/{id}")
    public String getWebToonEpisodeDetail(@PathVariable("id") Long episodeId, Model model,HttpSession session) {
        // 에피소드 정보 가져오기
        WebToonEpisode webToonEpisode = webToonEpisodeService.findById(episodeId);
        // 에피소드 이미지 가져오기
        List<EpisodeImage> episodeImages = webToonEpisodeService.getImagesByEpisodeId(episodeId);
        //댓글 목록 가져오기
        List<Comment> comments = commentService.getCommentByEpisode(webToonEpisode);

        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");



        model.addAttribute("comments", comments);
        model.addAttribute("episode", webToonEpisode);
        model.addAttribute("images", episodeImages);
        model.addAttribute("loginUser", siteUser);
        model.addAttribute("loginCompanyUser", companyUser);

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

    //웹툰 에피소드 삭제
    @GetMapping("/webtoon/episode/delete/{id}")
    public String deleteEpisode(@PathVariable("id") Long id,HttpSession session){
        // 로그인된 사용자 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null) {
            return "redirect:/login/user"; // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
        }

        WebToonEpisode webToonEpisode = this.webToonEpisodeService.getWebtoonEpisode(id);
        this.webToonEpisodeService.deleteEpisode(webToonEpisode);

        Long webToonId = webToonEpisode.getWebToon().getId();
        return String.format("redirect:/webtoon/episode/list/%d", webToonId);
    }

    //웹툰 에피소드 수정
    @GetMapping("/webtoon/episode/modify/{id}")
    public String modifyEpisode(@PathVariable("id") Long episodeId, Model model,HttpSession session) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        WebToonEpisode webToonEpisode = webToonEpisodeService.findById(episodeId);
        if (siteUser == null){
            return "redirect:/login/user";
        }
        if (!webToonEpisode.getWebToon().getSiteUser().getId().equals(siteUser.getId())){
            return "access_denied";
        }

        WebToonEpisode episode = webToonEpisodeService.findById(episodeId);
        model.addAttribute("episode", episode);
        return "episode_modify";  // 수정 폼을 렌더링
    }
    @PostMapping("/webtoon/episode/modify/{id}")
    public String modifyWebToonEpisode(
            @PathVariable("id") Long episodeId,
            @RequestParam("title") String title,
            @RequestParam(value = "episodeImages", required = false) MultipartFile[] episodeImages,
            @RequestParam(value = "thumbnailImg", required = false) MultipartFile thumbnailImg) throws IOException {

        // WebToonEpisode 수정
        webToonEpisodeService.modifyWebToonEpisode(episodeId, title, episodeImages, thumbnailImg);

        return "redirect:/webtoon/episode/list/" + webToonEpisodeService.findById(episodeId).getWebToon().getId(); // 에피소드 목록 페이지로 리다이렉트
    }

}
