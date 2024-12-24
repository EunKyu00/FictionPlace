//package com.example.fiction_place1.domain.user.controller;
//
//import com.example.fiction_place1.domain.user.entity.CompanyUser;
//import com.example.fiction_place1.domain.user.entity.SiteUser;
//import com.example.fiction_place1.domain.user.service.CompanyUserService;
//import com.example.fiction_place1.domain.user.service.SiteUserService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@RequiredArgsConstructor
//@Controller
//public class LoginController {
//    private final SiteUserService siteUserService;
//    private final CompanyUserService companyUserService;
//
//    @GetMapping("/login")
//    public String loginForm() {
//        return "user_login"; // 통합된 로그인 페이지
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam("userType") String userType,
//                        @RequestParam("username") String username,
//                        @RequestParam("password") String password,
//                        HttpSession session,
//                        Model model) {
//        // 입력값 검증
//        if (username == null || username.trim().isEmpty()) {
//            model.addAttribute("errorUserName", "아이디를 입력해주세요.");
//        }
//        if (password == null || password.trim().isEmpty()) {
//            model.addAttribute("errorPassword", "비밀번호를 입력해주세요.");
//        }
//        if (model.containsAttribute("errorUserName") || model.containsAttribute("errorPassword")) {
//            return "user_login";
//        }
//
//        try {
//            if ("USER".equals(userType)) {
//                // 일반회원 로그인 처리
//                SiteUser siteUser = siteUserService.login(username, password);
//                session.setAttribute("loginUser", siteUser);
//
//                // 프로필 이미지 설정
//                if (siteUser.getProfileImageUrl() != null) {
//                    session.setAttribute("profileImageUrl", siteUser.getProfileImageUrl());
//                } else {
//                    session.setAttribute("profileImageUrl", "/unnamed.png");
//                }
//
//            } else if ("COMPANY".equals(userType)) {
//                // 기업회원 로그인 처리
//                CompanyUser companyUser = companyUserService.login(username, password);
//                session.setAttribute("loginCompanyUser", companyUser);
//            }
//
//            return "redirect:/"; // 성공 시 홈으로 리다이렉트
//
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "user_login"; // 에러 시 로그인 페이지로 돌아감
//        }
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate(); // 세션 무효화
//        return "redirect:/";
//    }
//}
