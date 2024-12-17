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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class MyProfileController {
    @Autowired
    private final MyProfileService myProfileService;
    private final SiteUserService siteUserService;
    private final CompanyUserService companyUserService;

    // 일반 사용자 프로필 보기
    @GetMapping("/profile/user/{id}")
    public String getUserProfile(@PathVariable("id") Long id , Model model) {
        //id로 사용자를 조회
        SiteUser siteUser = siteUserService.findById(id);


        if (siteUser != null) {
            model.addAttribute("nickname", siteUser.getNickname());
            model.addAttribute("email", siteUser.getEmail());
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
    @PostMapping("/profile/user/{id}/upload-image")
    public ResponseEntity<String> uploadImage(
            @PathVariable("id") Long userId, // URL에서 사용자 ID 가져옴
            HttpSession session,             // 세션에서 로그인된 사용자 확인
            @RequestParam("image") MultipartFile imageFile // 업로드된 이미지 파일
    ) {
        // 세션에서 로그인된 사용자 정보 가져오기
        SiteUser loggedInUser = (SiteUser) session.getAttribute("loginUser");
        if (loggedInUser == null || !loggedInUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("접근 권한이 없습니다.");
        }

        // 파일 이름 생성 (사용자 ID 기반)
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/upload/";
        String fileName = userId + "_" + imageFile.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_");
        File destinationFile = new File(uploadDir, fileName);

        try {
            // 업로드 디렉토리 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("업로드 디렉토리 생성 실패");
            }

            // 파일 저장
            imageFile.transferTo(destinationFile);

            // 데이터베이스에 이미지 경로 저장
            myProfileService.updateProfileImage(userId, "/upload/" + fileName);
            session.setAttribute("profileImageUrl", "/upload/" + fileName);
            System.out.println("파일 저장 경로: " + destinationFile.getAbsolutePath());
            System.out.println("URL로 접근할 경로: " + "/upload/" + fileName);
            return ResponseEntity.ok("/upload/" + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 저장 실패: " + e.getMessage());
        }
    }

    @GetMapping("/upload/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
        try {
            // 파일 경로 설정
            Path file = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/upload").resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            // 파일이 존재하지 않으면 404 반환
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Content-Type 설정 (이미지 파일)
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //일반 유저 정보 변경
    @GetMapping("/profile/user/{id}/modify")
    public String userModifyForm (@PathVariable("id") Long id, HttpSession session, Model model) {
        SiteUser loginUser = siteUserService.getLoggedInUser(session);
        if(!loginUser.getId().equals(id)){
            throw new IllegalStateException("접근권한 없음");
        }
         model.addAttribute("user", loginUser);
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