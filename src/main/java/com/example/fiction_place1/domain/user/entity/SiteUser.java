package com.example.fiction_place1.domain.user.entity;


import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "site_user")
public class SiteUser extends BaseEntity {
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String thumbnailImg;

    private Boolean isSocialLogin = false;

    @OneToMany(mappedBy = "siteUser", fetch = FetchType.EAGER)
    private List<Message> messages;

    @OneToMany(mappedBy = "siteUser", fetch = FetchType.EAGER)
    private List<Board> boards;

    @OneToMany(mappedBy = "siteUser")
    private List<WebToon> webToons;

    @OneToMany(mappedBy = "siteUser")
    private List<Comment> comments;

    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.ALL)
    private MyProfile myProfile; // MyProfile과 연결

    @Column(nullable = false)
    private String role; // 사용자 역할 (USER, COMPANY, ADMIN 등)
}

