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
    public String getUserProfile(@PathVariable("id") Long id, Model model) {
        // id로 사용자를 조회
        SiteUser siteUser = siteUserService.findById(id); // id로 사용자 조회

        if (siteUser != null) {
            model.addAttribute("nickname", siteUser.getNickname());
            model.addAttribute("email", siteUser.getEmail());
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
        }
        return "myprofile"; // myprofile.html로 이동
    }

    //기업회원 프로필
    @GetMapping("/profile/company/{id}")
    public String getCompanyProfile(@PathVariable("id") Long id, Model model) {
        // id로 기업 사용자를 조회
        CompanyUser companyUser = companyUserService.findById(id); // id로 기업 사용자 조회

        if (companyUser != null) {
            model.addAttribute("companyName", companyUser.getCompanyName());
            model.addAttribute("email", companyUser.getEmail());
            // 추가적인 기업회원 정보도 모델에 담을 수 있습니다.
        } else {
            model.addAttribute("message", "회사를 찾을 수 없습니다.");
        }

        return "myprofile"; // myprofile.html로 이동
    }

    //작가 프로필 버튼 프로필(작가 작품 정보)
    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable("id") Long id, Model model) {
        // id로 사용자를 조회
        SiteUser siteUser = siteUserService.findById(id); // id로 사용자 조회

        if (siteUser != null) {
            model.addAttribute("siteUser", siteUser);
            model.addAttribute("nickname", siteUser.getNickname());
            model.addAttribute("email", siteUser.getEmail());
            // 작가의 웹툰 목록을 모델에 추가
            model.addAttribute("webtoons", siteUser.getWebToons());
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
        }

        return "profile"; // profile.html로 이동
    }


    @PostMapping("/profile/user/{id}/upload-image")
    public ResponseEntity<String> uploadImage(
            @PathVariable("id") Long userId, // URL에서 사용자 ID 가져옴
            HttpSession session,             // 세션에서 로그인된 사용자 확인
            @RequestParam("image") MultipartFile imageFile // 업로드된 이미지 파일
    ) {
        // 세션에서 로그인된 사용자 정보 가져오기
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        if (siteUser == null || !siteUser.getId().equals(userId)) {
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

    @GetMapping("/profile/user/modify")
    public String userModifyForm(HttpSession session, Model model) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser != null) {
            model.addAttribute("user", siteUser);
        }

        return "modify_user"; // 수정화면
    }

    @PostMapping("/profile/user/modify")
    public String updateUser(HttpSession session,
                             @RequestParam("nickname") String nickname,
                             @RequestParam("email") String email,
                             @RequestParam(value = "password", required = false) String password,
                             Model model) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");

        if (siteUser != null) {
            try {
                siteUserService.modifySiteUser(siteUser, nickname, email, password);
                model.addAttribute("user", siteUser); // 수정된 user 객체를 다시 model에 추가
            } catch (IllegalArgumentException e) {
                // 예외 발생 시 errorMessage에 에러 메시지를 담아서 전달
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("user", siteUser); // 예외 발생 시 user 객체도 다시 전달
                return "modify_user";  // 수정 화면으로 돌아감
            }
        }

        // 수정된 사용자 프로필 페이지로 리다이렉트
        return "redirect:/profile/user/" + siteUser.getId();
    }


    @GetMapping("/profile/companyUser/modify")
    public String updateCompanyUser(HttpSession session, Model model){
        CompanyUser companyUser = (CompanyUser)session.getAttribute("loginCompanyUser");

        if (companyUser != null){
            model.addAttribute("companyUser",companyUser);
        }
        return "modify_companyUser";
    }

    @PostMapping("/profile/companyUser/modify")
    public String updateCompanyUser(HttpSession session, @RequestParam("companyName") String companyName,
                                    @RequestParam("email") String email,
                                    @RequestParam(value = "password", required = false) String password,
                                    Model model){
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (companyUser != null) {
            try {
                companyUserService.modifyCompanyUser(companyUser,companyName,email,password);
                model.addAttribute("companyUser",companyUser);
            }catch (IllegalArgumentException e){
                model.addAttribute("errorMessage",e.getMessage());
                model.addAttribute("companyUser",companyUser);
                return "modify_companyUser";
            }
        }
        return "redirect:/profile/company/" + companyUser.getId();
    }

}