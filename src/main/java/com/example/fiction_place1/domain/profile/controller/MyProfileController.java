package com.example.fiction_place1.domain.profile.controller;

import com.example.fiction_place1.domain.profile.service.MyProfileService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.service.CompanyUserService;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class MyProfileController {
    @Autowired
    private final MyProfileService myProfileService;
    private final SiteUserService siteUserService;
    private final CompanyUserService companyUserService;
    // 일반 사용자 프로필 보기
    @GetMapping("/profile/user/{id}")
    public String getUserProfile(@PathVariable("id") Long id , Model model, HttpSession session) {
        //id로 사용자를 조회
        SiteUser siteUser = siteUserService.findById(id);
        SiteUser loggedInUser = siteUserService.getLoggedInUser(session);
        if (loggedInUser != null) {
            System.out.println("로그인된 사용자 ID: " + loggedInUser.getId());
            model.addAttribute("loggedInUserId", loggedInUser.getId());
        } else {
            System.out.println("로그인된 사용자 없음");
            model.addAttribute("loggedInUserId", null);
        }
        if (siteUser != null) {
            model.addAttribute("nickname", siteUser.getNickname());
            model.addAttribute("email", siteUser.getEmail());
            model.addAttribute("description", siteUser.getMyProfile().getDescription());
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
        }

        return "myprofile";
    }

    // 기업 프로필 보기
    @GetMapping("/profile/company/{id}")
    public String getCompanyProfile(@PathVariable("id") Long id, Model model) {
        //id로 기업 조회
        CompanyUser companyUser = companyUserService.findById(id);

        if (companyUser != null) {
            model.addAttribute("companyName", companyUser.getCompanyName());
            model.addAttribute("email", companyUser.getEmail());
        } else {
            model.addAttribute("message", "회사를 찾을 수 없습니다.");
        }

        return "myprofile";
    }

    //일반 유저 정보 변경
    @GetMapping("/profile/user/{id}/modify")
    public String userModifyForm (@PathVariable("id") Long id, HttpSession session, Model model) {
        SiteUser loggedInUser = siteUserService.getLoggedInUser(session);
        if(!loggedInUser.getId().equals(id)){
            throw new IllegalStateException("접근권한 없음");
        }
        if(loggedInUser!= null) {
            model.addAttribute("user", loggedInUser);
        }
        return "modify_user"; //수정화면
    }

    @PostMapping("/profile/user/{id}/modify")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute SiteUser updateUser, @RequestParam(value = "profileImage", required = false) MultipartFile file, HttpSession session) {
        SiteUser loginUser = siteUserService.getLoggedInUser(session);
        if (!loginUser.getId().equals(id)) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }
        siteUserService.updateUser(loginUser, updateUser);
        return "redirect:/profile/user/{id}";
    }
}