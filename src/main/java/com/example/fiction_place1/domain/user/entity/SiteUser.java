package com.example.fiction_place1.domain.user.entity;




import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.favorite.entity.Favorite;
import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "site_user")

public class SiteUser extends BaseEntity implements User {
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String thumbnailImg;

    private Boolean isSocialLogin = false;

    @OneToMany(mappedBy = "senderSiteUser")
    private List<Message> senderMessages; // 회사가 보낸 메시지 목록

    @OneToMany(mappedBy = "receiverSiteUser")
    private List<Message> receiverMessages;

    @OneToMany(mappedBy = "siteUser")
    private List<Board> boards;

    @OneToMany(mappedBy = "siteUser")
    private List<WebToon> webToons;

    @OneToMany(mappedBy = "siteUser")
    private List<Comment> comments;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favoriteWebtoons = new ArrayList<>();

    @Override
    public Long getId() {
        return this.id;  // BaseEntity에서 상속받은 id 반환
    }
    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.ALL)
    private MyProfile myProfile; // MyProfile과 연결

    @Column(nullable = false)
    private String role; // 사용자 역할 (USER, COMPANY, ADMIN 등)

    // MyProfile을 통해 프로필 이미지 URL을 반환
    public String getProfileImageUrl() {
        if (myProfile != null) {
            return myProfile.getProfileImage() != null ? myProfile.getProfileImage() : "/images/unnamed.png";
        }
        return "/images/unnamed.png"; // 기본 이미지 반환
    }

    public void setProfile(MyProfile profile) {
        this.myProfile = profile;
        if (profile != null) {
            profile.setSiteUser(this);
        }
    }
}

