//package com.example.fiction_place1.domain.favorite.controller;
//
//import com.example.fiction_place1.domain.favorite.entity.Favorite;
//import com.example.fiction_place1.domain.favorite.service.FavoriteService;
//import com.example.fiction_place1.domain.user.entity.SiteUser;
//import com.example.fiction_place1.domain.webtoon.entity.WebToon;
//import com.example.fiction_place1.domain.webtoon.service.WebToonService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class FavoriteController {
//    private final FavoriteService favoriteService;
//    private final WebToonService webToonService;
//
//    @GetMapping("/my/favorite/webtoon")
//    public String favoriteWebtoon(HttpSession session, Model model) {
//        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
//
//        if (siteUser == null) {
//            return "redirect:/login/user";  // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
//        }
//
//        // 사용자의 관심작품 목록 가져오기
//        List<Favorite> favorites = favoriteService.getFavoritesBySiteUser(siteUser);
//        model.addAttribute("favorites", favorites);
//
//        return "my_favorite_webtoon";  // 관심작품 목록을 표시할 페이지
//    }
//
//
//
//    // WebToonController.java
//    @PostMapping("/webtoon/favorite/{id}")
//    public String toggleFavorite(@PathVariable("id") Long webtoonId, HttpSession session) {
//        // 세션에서 로그인된 사용자 가져오기
//        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
//
//        if (siteUser == null) {
//            return "redirect:/login/user";  // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
//        }
//
//        // 웹툰 ID로 웹툰 찾기
//        WebToon webToon = webToonService.findById(webtoonId);
//
//        // 관심작품 등록/취소 처리
//        favoriteService.toggleFavorite(siteUser, webToon);
//
//        // 웹툰 상세 페이지로 리다이렉트 (또는 원하는 페이지로 리다이렉트)
//        return "redirect:/webtoon/detail/" + webtoonId;
//    }
//
//
//}
