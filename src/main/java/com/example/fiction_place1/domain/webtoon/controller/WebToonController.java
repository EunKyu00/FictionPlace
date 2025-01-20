package com.example.fiction_place1.domain.webtoon.controller;

import com.example.fiction_place1.domain.favorite.service.FavoriteService;
import com.example.fiction_place1.domain.genre_type.entity.GenreType;
import com.example.fiction_place1.domain.genre_type.service.GenreTypeService;
import com.example.fiction_place1.domain.recommend.service.RecommendService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.form.WebToonForm;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import com.example.fiction_place1.domain.webtoon.service.FileService;
import com.example.fiction_place1.domain.webtoon.service.WebToonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebToonController {

    private final WebToonService webToonService;
    private final GenreTypeService genreTypeService;
    private final FileService fileService;  // FileService 추가
    private final RecommendService recommendService;
    private final WebToonRepository webToonRepository;
    private final FavoriteService favoriteService;


    //본인 작품 관리
    @GetMapping("/my/webtoon")
    public String myWebtoon(Model model,HttpSession session,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "3") int size){
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
                                @RequestParam(value = "thumbnailImg", required = false) MultipartFile thumbnailImg,
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
            webToonService.createWebToon(webToonForm.getTitle(), webToonForm.getContent(),
                    webToonForm.getGenreTypeId(), siteUser, thumbnailImg);  // 경로 전달
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

    @PostMapping("/webtoon/recommend/{id}")
    @ResponseBody
    public Map<String, Object> likeWebToon(@PathVariable("id") Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
            CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");
            WebToon webToon = webToonService.findById(id);

            if (webToon == null) {
                response.put("success", false);
                response.put("message", "웹툰을 찾을 수 없습니다.");
                return response;
            }

            // 로그인 여부 확인
            if (siteUser == null && companyUser == null) {
                response.put("success", false);
                response.put("message", "로그인 후 이용해주세요.");
                return response;
            }

            // 추천 여부 확인
            boolean hasRecommended = false;
            if (siteUser != null) {
                hasRecommended = recommendService.hasSiteUserRecommended(siteUser, webToon);
            } else if (companyUser != null) {
                hasRecommended = recommendService.hasCompanyUserRecommended(companyUser, webToon);
            }

            if (hasRecommended) {
                response.put("success", false);
                response.put("message", "이미 추천하셨습니다!");
            } else {
                if (siteUser != null) {
                    recommendService.addSiteUserRecommendation(siteUser, webToon);
                } else if (companyUser != null) {
                    recommendService.addCompanyUserRecommendation(companyUser, webToon);
                }
                response.put("success", true);
                response.put("message", "추천이 완료되었습니다!");
                response.put("likes", webToon.getLikes()); // 추천 수 갱신
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "추천 처리 중 오류가 발생했습니다.");
            e.printStackTrace();  // 예외 로그 출력
        }
        return response;
    }



    //관심작품 등록 처리
    @PostMapping("/webtoon/favorite/{id}")
    public String toggleFavorite(@PathVariable("id") Long id, HttpSession session, Model model,RedirectAttributes redirectAttributes) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser"); // 세션에서 로그인한 사용자 정보 가져오기
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 후 이용해주세요.");
            return String.format("redirect:/main/page/webtoon/episode/%s", id); // 웹툰 상세 페이지로 리디렉션
        }

        if (siteUser != null) {
            favoriteService.toggleFavorite(id, siteUser);
        } else if (companyUser != null) {
            favoriteService.toggleFavorite(id, companyUser);
        }
        // 해당 웹툰을 즐겨찾기 목록에서 확인
        WebToon webToon = webToonRepository.findById(id).orElse(null);
        boolean favorite = false;

        if (webToon != null) {
            // 즐겨찾기 목록에서 웹툰을 찾는 로직을 두 가지 경우로 분리
            if (siteUser != null) {
                favorite = favoriteService.getFavoriteWebToons(siteUser).contains(webToon);
            } else if (companyUser != null) {
                favorite = favoriteService.getFavoriteWebToons(companyUser).contains(webToon);
            }
        }

        model.addAttribute("favorite", favorite);  // favorite 상태를 모델에 추가
        return String.format("redirect:/main/page/webtoon/episode/%s", id); // 웹툰 상세 페이지로 리디렉션
    }


    //본인 프로필 관심작품 메뉴
    @GetMapping("/my/favorite/webtoon")
    public String getMyFavoriteWebtoon(HttpSession session, Model model) {

        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null) {
            return "redirect:/login/user"; // 로그인하지 않은 경우
        }

        if (siteUser != null){
            List<WebToon> favoriteWebtoons = favoriteService.getFavoriteWebToons(siteUser);
            model.addAttribute("favoriteWebtoons", favoriteWebtoons);
        } else if (companyUser != null){
            List<WebToon> favoriteWebtoons = favoriteService.getFavoriteWebToons(companyUser);
            model.addAttribute("favoriteWebtoons", favoriteWebtoons);
        }

        return "my_favorite_webtoon";
    }

    //웹툰 검색 기능
    @GetMapping("/webtoon/search")
    public String webToonSearch(Model model,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "page", defaultValue = "0") int page,  // 페이지 번호
                                @RequestParam(value = "size", defaultValue = "10") int size) {  // 페이지 크기

        // Pageable 객체 생성 (page, size, 정렬 기준 설정)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("likes")));  // likes 기준 내림차순으로 정렬

        Page<WebToon> searchResultsPage;

        // 전체 웹툰 목록 가져오기 (검색어가 없을 경우)
        if (keyword == null || keyword.trim().isEmpty()) {
            model.addAttribute("nullMessage", "※ 검색어를 입력해주세요. ※");

            // 페이지네이션을 위한 전체 웹툰 목록
            searchResultsPage = webToonService.findAll(pageable);
            model.addAttribute("selectedWebtoons", searchResultsPage.getContent()); // 해당 페이지의 웹툰 목록만 표시
            model.addAttribute("keyword", ""); // 검색어를 빈 값으로 설정
        } else {
            // 검색어가 있을 경우 검색된 웹툰 목록을 가져오기 (페이징 적용)
            searchResultsPage = webToonService.searchWebToon(keyword, pageable);

            if (searchResultsPage.getTotalElements() == 0) {
                model.addAttribute("searchErrorMessage", "※ 검색된 웹툰이 없습니다. ※");
                // 검색 결과가 없을 때 전체 목록 표시
                searchResultsPage = webToonService.findAll(pageable);
                model.addAttribute("selectedWebtoons", searchResultsPage.getContent());
            } else {
                model.addAttribute("selectedWebtoons", searchResultsPage.getContent()); // 검색된 웹툰 목록만 표시
            }
            model.addAttribute("keyword", keyword); // 검색어 유지
        }

        // 페이징 관련 속성들 추가
        model.addAttribute("currentPage", searchResultsPage.getNumber()); // 현재 페이지
        model.addAttribute("totalPages", searchResultsPage.getTotalPages()); // 전체 페이지 수
        model.addAttribute("pageSize", size); // 한 페이지에 표시할 웹툰 수

        return "webtoon_list";  // 웹툰 리스트를 보여줄 뷰 이름
    }


    @GetMapping("/webtoons")
    public String getWebtoonsByGenre(
            @RequestParam(name = "genreId", required = false) Long genreId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<WebToon> webtoonsPage;

        if (genreId != null) {
            // 장르 ID가 있으면 해당 장르의 웹툰만 조회 (페이징 적용)
            webtoonsPage = webToonService.getWebtoonsByGenreId(genreId, pageable);
            model.addAttribute("selectedGenreId", genreId);
        } else {
            // 장르 ID가 없으면 모든 웹툰 조회 (페이징 적용)
            webtoonsPage = webToonService.findAll(pageable);
        }

        List<WebToon> webtoons = webtoonsPage.getContent();
        model.addAttribute("selectedWebtoons", webtoons);
        model.addAttribute("currentPage", webtoonsPage.getNumber());
        model.addAttribute("totalPages", webtoonsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        // 장르 선택에 따른 메시지 전달
        String nullMessage = (genreId != null) ? "선택된 장르의 웹툰이 없습니다." : "모든 웹툰을 확인하세요.";
        model.addAttribute("nullMessage", nullMessage);

        return "webtoon_list";
    }

    @GetMapping("/webtoon/likes")
    public String getWebtoonsSortedByLikes(
            @RequestParam(name = "genreId", required = false) Long genreId,
            @RequestParam(value = "page", defaultValue = "0") int page,  // 페이지 번호
            @RequestParam(value = "size", defaultValue = "10") int size, // 페이지 크기
            Model model) {

        Pageable pageable = PageRequest.of(page, size);  // Pageable 객체 생성
        Page<WebToon> webtoonsPage;

        if (genreId != null) {
            // 장르 ID가 있을 경우 해당 장르의 웹툰만 추천순으로 가져오기 (페이징 적용)
            webtoonsPage = webToonService.getWebtoonsByGenreSortedByLikes(genreId, pageable);
            model.addAttribute("selectedGenreId", genreId);
        } else {
            // 장르 ID가 없을 경우 전체 웹툰을 추천순으로 가져오기 (페이징 적용)
            webtoonsPage = webToonService.getWebtoonsSortedByLikes(pageable);
        }

        List<WebToon> webtoons = webtoonsPage.getContent();  // 현재 페이지의 웹툰 목록
        model.addAttribute("selectedWebtoons", webtoons);
        model.addAttribute("currentPage", webtoonsPage.getNumber());
        model.addAttribute("totalPages", webtoonsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "webtoon_like_list"; // 웹툰 리스트를 보여줄 뷰 이름
    }

    @GetMapping("/aaa")
    public String aaa (){
        return "aaa";
    }
}




