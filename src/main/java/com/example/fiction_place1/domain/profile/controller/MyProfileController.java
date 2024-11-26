package com.example.fiction_place1.domain.profile.controller;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.service.MyProfileService;
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

    @GetMapping("/profile/user/{username}")
    public String getMyProfile(@PathVariable("username") String username, Model model) {
        MyProfile profile = myProfileService.getProfileBySiteUser(username);
        model.addAttribute("profile", profile);
        return "myprofile";
    }

    @GetMapping("/profile/company/{email}")
    public String getCompanyProfile(@PathVariable("email") String email, Model model) {
        MyProfile profile = myProfileService.getProfileByCompanyUser(email);
        model.addAttribute("profile", profile);
        return "myprofile";
    }

}
