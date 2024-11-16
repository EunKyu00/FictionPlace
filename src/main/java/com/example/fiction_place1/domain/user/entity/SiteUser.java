package com.example.fiction_place1.domain.user.entity;




import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.comment.entity.Comment;
import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "siteUser")
    private List<Message> messages;

    @OneToMany(mappedBy = "siteUser")
    private List<Board> boards;

    @OneToMany(mappedBy = "siteUser")
    private List<WebToon> webToons;

    @OneToMany(mappedBy = "siteUser")
    private List<Comment> comments;


}

