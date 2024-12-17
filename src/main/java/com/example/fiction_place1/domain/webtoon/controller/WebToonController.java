package com.example.fiction_place1.domain.webtoon.controller;

import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.service.GenreTypeService;
import com.example.fiction_place1.domain.recommend.service.RecommendService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.form.WebToonForm;
import com.example.fiction_place1.domain.webtoon.service.FileService;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import com.example.fiction_place1.domain.webtoon_episode.service.WebToonEpisodeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebToonController {

    private final WebToonService webToonService;
    private final GenreTypeService genreTypeService;
    private final FileService fileService;  // FileService 추가
    private final RecommendService recommendService;



    //본인 작품 관리
    @GetMapping("/my/webtoon")
    public String myWebtoon(Model model,HttpSession session ){
        // 세션에서 로그인된 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");


        if (siteUser == null ){
            return "redirect:/login/user";
        }
        if (siteUser != null ){
            List<WebToon> webtoons = webToonService.getWebtoonsByUser(siteUser);
            model.addAttribute("webtoons", webtoons);  // 웹툰 목록을 모델에 추가
        }
        return "my_webtoon";
    }

    // 최초 웹툰 등록
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/webtoon/create")
    public String webToonCreate(WebToonForm webToonForm, Model model,HttpSession session) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null){
            return "redirect:/login/user";
        }

        List<GenreType> genreTypes = genreTypeService.getAllGenres();
        model.addAttribute("genreTypes", genreTypes);
        model.addAttribute("webToonForm", new WebToonForm());
        return "webtoon_create_form";
    }

    // 최초 웹툰 등록 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/webtoon/create")
    public String webToonCreate(@Valid WebToonForm webToonForm, BindingResult bindingResult,
                                HttpSession session,
                                @RequestParam("thumbnailImg") MultipartFile thumbnailImg,
                                Model model) throws IOException {

        // 유효성 검사 실패 시 폼과 장르 목록을 다시 보여줌
        if (bindingResult.hasErrors()) {
            List<GenreType> genreTypes = genreTypeService.getAllGenres();
            model.addAttribute("genreTypes", genreTypes);
            model.addAttribute("webToonForm", webToonForm);
            return "webtoon_create_form";  // 오류가 있으면 폼으로 돌아감
        }

        // 세션에서 로그인된 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser != null) {
            // 이미지 업로드 후 경로를 가져오기
            String thumbnailPath = fileService.uploadImage(thumbnailImg);  // 이미지 경로 얻기
            // 웹툰 생성 처리 (이미지 경로를 포함)
            webToonService.createWebToon(webToonForm.getTitle(), webToonForm.getContent(),
                    webToonForm.getGenreTypeId(), siteUser, thumbnailPath);  // 경로 전달
        }

        // 성공적으로 등록된 후 리다이렉트
        return "redirect:/";  // 메인 페이지로 리다이렉트
    }

    //웹툰 삭제
    @GetMapping("/webtoon/delete/{id}")
    public String deleteWebToon(@PathVariable("id") Long id, HttpSession session){
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser == null){
            return "redirect:/login/user";
        }

        WebToon webToon = webToonService.findById(id);
        webToonService.deleteWebtoon(webToon);
        return "redirect:/my/webtoon";
    }

    //웹툰 수정
    @GetMapping("/webtoon/modify/{id}")
    public String modifyWebToon(@PathVariable("id") Long id,Model model,HttpSession session){
        WebToon webtoon = webToonService.findById(id);
        List<GenreType> genreTypes = genreTypeService.getAllGenres();

        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null){
            return "redirect:/login/user";
        }
        if (!webtoon.getSiteUser().getId().equals(siteUser.getId())){
            return "access_denied";
        }

        model.addAttribute("genreTypes", genreTypes);
        model.addAttribute("webtoon",webtoon);

        return "webtoon_modify";
    }

    //웹툰 수정 처리
    @PostMapping("/webtoon/modify/{id}")
    public String modifyWebToon(@PathVariable("id") Long webtoonId,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("genreTypeId") Long genreTypeId,
                                @RequestParam(value = "thumbnailImg", required = false) MultipartFile thumbnailImg) throws IOException {

        webToonService.modifyWebToon(webtoonId, title, content, genreTypeId, thumbnailImg);  // genreTypeId도 전달
        return "redirect:/my/webtoon";  // 수정 후 리다이렉트 할 페이지 (예: 웹툰 목록 페이지)
    }

    //웹툰 추천
    @PostMapping("/webtoon/recommend/{id}")
    public String likeWebToon(@PathVariable("id") Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");
        WebToon webToon = webToonService.findById(id);

        if (siteUser == null && companyUser == null){
            redirectAttributes.addFlashAttribute("message","로그인 후 이용해주세요");
            return String.format("redirect:/main/page/webtoon/episode/%s", id);
        }
        // 추천 여부 확인
        boolean hasRecommended = false;

        if (siteUser != null) {
            hasRecommended = recommendService.hasSiteUserRecommended(siteUser, webToon);
        } else if (companyUser != null) {
            hasRecommended = recommendService.hasCompanyUserRecommended(companyUser, webToon);
        }

        if (hasRecommended) {
            redirectAttributes.addFlashAttribute("message", "이미 추천하셨습니다!");
        } else {
            if (siteUser != null) {
                recommendService.addSiteUserRecommendation(siteUser, webToon);
            } else if (companyUser != null) {
                recommendService.addCompanyUserRecommendation(companyUser, webToon);
            }
            redirectAttributes.addFlashAttribute("message", "추천이 완료되었습니다!");
        }

        return String.format("redirect:/main/page/webtoon/episode/%s", id);
    }
}




