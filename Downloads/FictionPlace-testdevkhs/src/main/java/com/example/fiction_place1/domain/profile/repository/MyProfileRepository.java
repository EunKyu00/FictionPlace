package com.example.fiction_place1.domain.profile.repository;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyProfileRepository extends JpaRepository<MyProfile, Long> {
    //일반유저 정보 (닉네임, 이메일, 자기소개)
    MyProfile findBySiteUser_Username(String username);
    MyProfile findByDescription(String description);

    MyProfile findByCompanyUser_Email(String email);

    Optional<MyProfile> findBySiteUserId(Long siteUserId);
    Optional<MyProfile> findByCompanyUserId(Long companyuserId);

    // SiteUser 객체를 기준으로 프로필 찾기
    Optional<MyProfile> findBySiteUser(SiteUser siteUser);

    @Override
    Optional<MyProfile> findById(Long userId);
}
