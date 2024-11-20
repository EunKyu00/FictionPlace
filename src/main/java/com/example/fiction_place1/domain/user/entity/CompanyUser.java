package com.example.fiction_place1.domain.user.entity;


import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.message.entity.Message;
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
public class CompanyUser extends BaseEntity {

    @Column(unique = true)
    private String companyName;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String businessRegistrationNumber; //사업자등록 번호

    @Column(nullable = false)
    private String contactPerson; //담당자 이름

    @OneToMany(mappedBy = "companyUser")
    private List<Message> messages;

    @OneToMany(mappedBy = "companyUser")
    private List<Board> boards;

}
