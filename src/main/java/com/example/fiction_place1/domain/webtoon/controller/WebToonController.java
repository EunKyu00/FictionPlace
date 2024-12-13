package com.example.fiction_place1.domain.webtoon.controller;

import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.service.GenreTypeService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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



    //본인 작품 관리
    @GetMapping("/my/webtoon")
    public String myWebtoon(Model model,HttpSession session ){
        // 세션에서 로그인된 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser != null ){
            List<WebToon> webtoons = webToonService.getWebtoonsByUser(siteUser);
            model.addAttribute("webtoons", webtoons);  // 웹툰 목록을 모델에 추가
        }
        return "my_webtoon";
    }

    // 최초 웹툰 등록
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/webtoon/create")
    public String webToonCreate(WebToonForm webToonForm, Model model) {
        List<GenreType> genreTypes = genreTypeService.getAllGenres();
        model.addAttribute("genreTypes", genreTypes);  // genreTypes로 수정
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
        } else {
            // 로그인 안된 사용자 처리
            model.addAttribute("errorMessage", "로그인 후 웹툰을 등록할 수 있습니다.");
            return "webtoon_create_form";  // 로그인 페이지로 리다이렉트
        }

        // 성공적으로 등록된 후 리다이렉트
        return "redirect:/";  // 메인 페이지로 리다이렉트
    }
}




