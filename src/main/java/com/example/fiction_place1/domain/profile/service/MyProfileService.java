package com.example.fiction_place1.domain.profile.service;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.repository.MyProfileRepository;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class MyProfileService {
    @Autowired
    private MyProfileRepository myProfileRepository;
    //일반유저
    public MyProfile getProfileBySiteUser(String username) {
        return myProfileRepository.findBySiteUser_Username(username);
    }
    //기업
    public MyProfile getProfileByCompanyUser(String email) {
        return myProfileRepository.findByCompanyUser_Email(email);
    }

    public void saveProfile(MyProfile profile) {
        myProfileRepository.save(profile);
    }

    public MyProfile getProfileBySiteUser(Long siteUserId) {
        return myProfileRepository.findBySiteUserId(siteUserId).orElse(null);
    }

    public MyProfile getProfileByCompanyUser(Long companyUserId) {
        return myProfileRepository.findByCompanyUserId(companyUserId).orElse(null);
    }
    public boolean uploadProfileImage(Long userId, MultipartFile imageFile) {
        try {
            // 사용자 프로필 가져오기
            Optional<MyProfile> profileOptional = myProfileRepository.findById(userId);
            if (profileOptional.isEmpty()) {
                return false;
            }

            MyProfile myProfile = profileOptional.get();

            // 파일 저장 경로 설정 (예: 서버의 특정 디렉터리에 저장)
            String uploadDir = "uploaded_images/";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String fileName = userId + "_" + imageFile.getOriginalFilename();
            File destinationFile = new File(uploadDir + fileName);

            // 파일 저장
            imageFile.transferTo(destinationFile);

            // 데이터베이스에 이미지 경로 저장
            myProfile.setProfileImage(fileName);
            myProfileRepository.save(myProfile);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProfileImagePath(Long userId) {
        Optional<MyProfile> profileOptional = myProfileRepository.findById(userId);
        return profileOptional.map(MyProfile::getProfileImage).orElse(null);
    }
    public SiteUser getLoggedInSiteUser(HttpSession session) {
        SiteUser loggedInSiteUser = (SiteUser) session.getAttribute("loginSiteUser");
        if(loggedInSiteUser == null) {
            throw new IllegalStateException("로그인된 사용자가 아닙니다");
        }
        return loggedInSiteUser;
    }
}
