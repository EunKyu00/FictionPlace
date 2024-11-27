package com.example.fiction_place1.domain.profile.controller;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.service.MyProfileService;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MyProfileController {
    @Autowired
    private final MyProfileService myProfileService;

    private final SiteUserService siteUserService;

    @GetMapping("/profile/user/{id}")
    public String getMyProfile(@PathVariable("id") Long id, HttpSession session, Model model) {
        SiteUser loggedInUser = siteUserService.getLoggedInUser(session);
        if (!loggedInUser.getId().equals(id)) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }

        MyProfile profile = myProfileService.getProfileBySiteUser(id);
        model.addAttribute("profile", profile);
        return "myprofile";
    }

    @GetMapping("/profile/company/{id}")
    public String getCompanyProfile(@PathVariable("id") String id, Model model) {
        MyProfile profile = myProfileService.getProfileByCompanyUser(id);
        model.addAttribute("profile", profile);
        return "myprofile";
    }

}
