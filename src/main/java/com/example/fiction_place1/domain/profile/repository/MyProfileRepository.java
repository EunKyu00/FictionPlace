package com.example.fiction_place1.domain.profile.repository;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyProfileRepository extends JpaRepository<MyProfile, Long> {
    MyProfile findBySiteUser_Username(String username);

    MyProfile findByCompanyUser_Email(String email);

    Optional<MyProfile> findBySiteUserId(Long siteUserId);
}
