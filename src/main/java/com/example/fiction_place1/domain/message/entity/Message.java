package com.example.fiction_place1.domain.message.entity;


import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "sender_site_user_id", nullable = true)
    private SiteUser senderSiteUser;

    @ManyToOne
    @JoinColumn(name = "sender_company_user_id", nullable = true)
    private CompanyUser senderCompanyUser;

    @ManyToOne
    @JoinColumn(name = "receiver_site_user_id", nullable = true)
    private SiteUser receiverSiteUser;

    @ManyToOne
    @JoinColumn(name = "receiver_company_user_id", nullable = true)
    private CompanyUser receiverCompanyUser;

    private String title;

    private String content;
}

