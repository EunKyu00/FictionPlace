package com.example.fiction_place1.domain.profile.entity;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MyProfile extends BaseEntity {

    @Column(nullable = true) // 프로필 이미지 경로 (필수 아님)
    private String profileImage;

    @Column(length = 500) // 자기소개 글 (최대 500자)
    private String description;

    //일반유저 id값 매핑
    @OneToOne
    @JoinColumn(name = "site_user_id") // 외래 키로 매핑
    private SiteUser siteUser;

    @Transient
    public String getNickname() {
        return siteUser != null ? siteUser.getNickname() : null;
    }

    @Transient
    public String getEmail() {
        return siteUser != null ? siteUser.getEmail() : null;
    }
    //기업 id값
    @OneToOne
    @JoinColumn(name = "company_user_id")
    private CompanyUser companyUser; // CompanyUser와의 연관 관계


    // 생성자 추가 (Lombok으로도 가능)
    public MyProfile(String profileImage, String description, CompanyUser companyUser) {
        this.profileImage = profileImage;
        this.description = description;
        this.companyUser = companyUser;
    }
}