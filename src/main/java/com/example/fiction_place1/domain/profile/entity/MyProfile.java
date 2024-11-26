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

    // 일반 사용자와 연결 (1:1 관계)
    @OneToOne
    @JoinColumn(name = "site_user_id", referencedColumnName = "id")
    private SiteUser siteUser;

    // 기업 사용자와 연결 (1:1 관계)
    @OneToOne
    @JoinColumn(name = "company_user_id", referencedColumnName = "id")
    private CompanyUser companyUser;

    // 생성자 추가 (Lombok으로도 가능)
    public MyProfile(String profileImage, String description, SiteUser siteUser, CompanyUser companyUser) {
        this.profileImage = profileImage;
        this.description = description;
        this.siteUser = siteUser;
        this.companyUser = companyUser;
    }
}