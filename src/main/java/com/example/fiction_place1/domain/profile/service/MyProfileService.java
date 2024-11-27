package com.example.fiction_place1.domain.profile.service;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.repository.MyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
