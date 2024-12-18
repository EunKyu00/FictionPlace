package com.example.fiction_place1.domain.user.entity;


import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.favorite.entity.Favorite;
import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.profile.entity.MyProfile;
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
public class CompanyUser extends BaseEntity implements User{

    @Column(unique = true)
    private String companyName;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String businessRegistrationNumber; //사업자등록 번호

    @Column(nullable = false)
    private String contactPerson; //담당자 이름

    @OneToMany(mappedBy = "companyUser",fetch = FetchType.EAGER)
    private List<Message> messages; // 회사가 보낸 메시지 목록

    @OneToMany(mappedBy = "companyUser")
    private List<Board> boards;

    @OneToMany(mappedBy = "companyUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favoriteWebtoons = new ArrayList<>();

    @Override
    public Long getId() {
        return this.id;  // BaseEntity에서 상속받은 id 반환
    }

    @OneToOne(mappedBy = "companyUser", cascade = CascadeType.ALL)
    private MyProfile myProfile; // MyProfile과 연결

    @Column(nullable = false)
    private String role; // 사용자 역할 (USER, COMPANY, ADMIN 등)

}
