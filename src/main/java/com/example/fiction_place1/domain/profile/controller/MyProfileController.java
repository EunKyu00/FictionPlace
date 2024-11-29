package com.example.fiction_place1.domain.profile.controller;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.repository.MyProfileRepository;
import com.example.fiction_place1.domain.profile.service.MyProfileService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.user.service.CompanyUserService;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MyProfileController {
    @Autowired
    private final MyProfileService myProfileService;
    private final SiteUserRepository siteUserRepository;
    private final CompanyUserService companyUserService;
    private final SiteUserService siteUserService;
    private final MyProfileRepository myProfileRepository;

    // 사용자 프로필 보기
    @GetMapping("/profile/user/{id}")
    public String getMyProfile(@PathVariable("id") Long id, HttpSession session, Model model) {
        SiteUser loggedInUser = siteUserService.getLoggedInUser(session);
        if (!loggedInUser.getId().equals(id)) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }

        MyProfile profile = myProfileService.getProfileBySiteUser(id);
        model.addAttribute("profile", profile);
        model.addAttribute("loggedInUserId", loggedInUser.getId()); // 사용자 ID를 모델에 추가
        return "myprofile";
    }

    // 회사 사용자 프로필 보기
    @GetMapping("/profile/company/{id}")
    public String getCompanyProfile(@PathVariable("id") Long id, HttpSession session, Model model) {
        CompanyUser loggedInCompanyUser = companyUserService.getLoggedInCompanyUser(session);

        if (!loggedInCompanyUser.getId().equals(id)) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }

        MyProfile profile = myProfileService.getProfileByCompanyUser(id);
        model.addAttribute("profile", profile);
        return "companyprofile";
    }

    @PostMapping("/profile/user/{id}/upload-image")
    @ResponseBody
    public ResponseEntity<String> uploadImage(
            @PathVariable("userId") Long userId,
            HttpSession session,
            @RequestParam("image") MultipartFile imageFile) {

        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null || !loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).body("접근 권한이 없습니다.");
        }

        String uploadDir = "upload/";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            boolean dirsCreated = uploadDirFile.mkdirs();
            if (!dirsCreated) {
                return ResponseEntity.status(500).body("디렉터리 생성 실패");
            }
        }

        String fileName = userId + "_" + imageFile.getOriginalFilename(); // 사용자 ID로 파일명 생성
        File destinationFile = new File(uploadDir + fileName);

        try {
            // 파일 저장
            imageFile.transferTo(destinationFile);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        }

        // 데이터베이스에 이미지 경로 저장 (my_profile 테이블에 저장)
        MyProfile myProfile = myProfileRepository.findById(userId).orElse(null);
        if (myProfile == null) {
            return ResponseEntity.status(404).body("프로필을 찾을 수 없습니다.");
        }

        myProfile.setProfileImage("/upload/" + fileName); // 저장된 파일 경로를 프로필 이미지로 설정
        myProfileRepository.save(myProfile);

        // 경로 반환
        return ResponseEntity.ok("/upload/" + fileName);
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<String> getProfileImagePath(@PathVariable("userId") Long userId, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null || !loggedInUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String imagePath = myProfileService.getProfileImagePath(userId);
        if (imagePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(imagePath);
    }
}