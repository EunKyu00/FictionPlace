package com.example.fiction_place1.domain.profile.service;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.repository.MyProfileRepository;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateProfileImage(Long userId, String imagePath) {
        MyProfile myProfile = myProfileRepository.findBySiteUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        myProfile.setProfileImage(imagePath);
        myProfileRepository.save(myProfile);
    }

    //일반유저 기본정보 가져오기 (닉네임, 자기소개, 이메일)

}
